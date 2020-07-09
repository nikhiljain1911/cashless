package com.example.cashless.Transactions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cashless.Constants
import com.example.cashless.DataService.transactionFromItem
import com.example.cashless.DataService.transactionToItem
import com.example.cashless.DataService.merchantData
import com.example.cashless.DataService.transactionMessage
import com.example.cashless.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_transaction_log.*

class TransactionLogActivity : AppCompatActivity() {


    var toMerchant: merchantData?=null
    val adapter= GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_log)

        transactionLogRV.adapter=adapter

        toMerchant=intent.getParcelableExtra<merchantData>(Constants.USER_KEY)
        supportActionBar?.title=toMerchant?.username

        listenForTransactions()

        transactionLogPayMoneyBtn.setOnClickListener{
            Log.d("TransactionLogActivity","try to send a message")
            performSendMessage()
        }


    }

    private fun listenForTransactions()
    {
        val fromId= FirebaseAuth.getInstance().uid
        val toId=toMerchant?.uid
        val ref= FirebaseDatabase.getInstance().getReference("transactions/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener
        {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                toMerchant=intent.getParcelableExtra<merchantData>(Constants.USER_KEY)
                val transactionmessage=snapshot.getValue(transactionMessage::class.java)
                if (transactionmessage!=null && toMerchant!=null) {
                    Log.d("TransactionLogActivity", transactionmessage.text)


                    if(transactionmessage.fromId== FirebaseAuth.getInstance().uid) {
                        val fromUser=LatestTransactionsActivity.currentUser
                        adapter.add(transactionFromItem(transactionmessage.text.toString(), fromUser!!))
                    }

                    else
                    {
                        adapter.add(transactionToItem(transactionmessage.text.toString(),
                            toMerchant!!))
                    }

                }
                transactionLogRV.scrollToPosition(adapter.itemCount-1)
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })





    }


    private fun performSendMessage()
    {
        //=intent.getParcelableExtra<merchantData>(Constants.USER_KEY)
        val text=transactionLogEnterMessage.text.toString()
        val fromId= FirebaseAuth.getInstance().uid.toString()
        val toId=toMerchant?.uid.toString()

        // val mref=FirebaseDatabase.getInstance().getReference("messages").push()
        val mref= FirebaseDatabase.getInstance().getReference("transactions/${fromId}/${toId}").push()
        val transactionmessage= transactionMessage(mref.key.toString(),text,fromId,toId,System.currentTimeMillis())
        mref.setValue(transactionmessage).addOnCompleteListener{
            Log.d("chatLogActivity","message stored: ${mref.key}")
            transactionLogEnterMessage.text.clear()
        }

        val toRef= FirebaseDatabase.getInstance().getReference("transactions/${toId}/${fromId}").push()
        toRef.setValue(transactionmessage)

        // do vaar store taa kita hai kyoki dova nu chahida hai ehh message

        val latestTransactionRef= FirebaseDatabase.getInstance().getReference("latest-transactions/$fromId/$toId")
        latestTransactionRef.setValue(transactionmessage)

        val toLatestTransactionRef= FirebaseDatabase.getInstance().getReference("latest-transactions/$toId/$fromId")
        toLatestTransactionRef.setValue(transactionmessage)

    }



}
package com.example.cashless.Transactions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cashless.Constants
import com.example.cashless.DataService.transactionMessage
import com.example.cashless.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_transaction_log.*
import kotlinx.android.synthetic.main.activity_transfer.*

class TransferActivity : AppCompatActivity() {

    var toMerchantId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        toMerchantId=intent.getStringExtra(Constants.USER_KEY)
        //val amount=amountTxt.text.toString()

        proceedToPayBtn.setOnClickListener {

             performSendMessage(toMerchantId!!)
        }

    }
    fun performSendMessage( toId:String)
    {

        val fromId= FirebaseAuth.getInstance().uid.toString()


        // val mref=FirebaseDatabase.getInstance().getReference("messages").push()
        val mref= FirebaseDatabase.getInstance().getReference("transactions/${fromId}/${toId}").push()
        val transactionmessage= transactionMessage(mref.key.toString(),amountTxt.text.toString(),fromId,toId,System.currentTimeMillis())
        mref.setValue(transactionmessage).addOnCompleteListener{
            Log.d("chatLogActivity","message stored: ${mref.key}")
            //amountTxt.text.clear()
            Toast.makeText(this,"paid successfully: ${amountTxt.text.toString()}",Toast.LENGTH_SHORT).show()
        }

        val toRef= FirebaseDatabase.getInstance().getReference("transactions/${toId}/${fromId}").push()
        toRef.setValue(transactionmessage)

        // do vaar store taa kita hai kyoki dova nu chahida hai ehh message

        val latestTransactionRef= FirebaseDatabase.getInstance().getReference("latest-transactions/$fromId/$toId")
        latestTransactionRef.setValue(transactionmessage)

        val toLatestTransactionRef= FirebaseDatabase.getInstance().getReference("latest-transactions/$toId/$fromId")
        toLatestTransactionRef.setValue(transactionmessage)

        val intent= Intent(this,LatestTransactionsActivity::class.java)
        startActivity(intent)



    }
}
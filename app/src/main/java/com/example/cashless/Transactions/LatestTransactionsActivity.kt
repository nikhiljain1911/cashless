package com.example.cashless.Transactions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.cashless.R
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.cashless.Constants
import com.example.cashless.Controllers.LoginActivity
import com.example.cashless.Controllers.MainActivity
import com.example.cashless.DataService.merchantData
import com.example.cashless.DataService.studentData
import com.example.cashless.DataService.transactionMessage
import com.example.cashless.LatestTransactionRow
import com.example.cashless.Transactions.NewTransactionActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_transactions.*
import kotlinx.android.synthetic.main.latest_transactions_row.view.*

class LatestTransactionsActivity : AppCompatActivity() {

    companion object
    {
        var currentUser:studentData?=null
    }


    val latestMessagesMap=HashMap<String,transactionMessage>()
    lateinit var auth:FirebaseAuth
    val adapter=GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_transactions)
        auth= Firebase.auth
        supportActionBar?.title="Recents"
        verifyUser()
        latestTransactionsRV.adapter=adapter

        adapter.setOnItemClickListener { item, view ->
            val intent=Intent(this,TransactionLogActivity::class.java)
            val row=item as LatestTransactionRow
            intent.putExtra(Constants.USER_KEY,row.user)
            startActivity(intent)
        }

        latestTransactionsRV.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        listenForLatestMessages()

        fetchCurrentUser()

    }

     private fun refreshRecyclerViewMessages()
     {
         adapter.clear()
         latestMessagesMap.values.forEach {
             adapter.add(LatestTransactionRow(it))
         }
     }

    private fun listenForLatestMessages()
    {
        val fromId=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("latest-transactions/$fromId")
        ref.addChildEventListener(object :ChildEventListener
        {
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val transactionmessage=snapshot.getValue(transactionMessage::class.java)?:return
//                if(latestMessagesMap[snapshot.key!!]!=null)
//                {
//                    latestMessagesMap.remove(snapshot.key!!)
//                }
                latestMessagesMap[snapshot.key!!] =transactionmessage
                refreshRecyclerViewMessages()
              //  adapter.add(LatestMessageRow(chatmessage!!))
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val transactionmessage=snapshot.getValue(transactionMessage::class.java)?:return

                latestMessagesMap[snapshot.key!!]=transactionmessage
                refreshRecyclerViewMessages()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun fetchCurrentUser()
    {
        val uid=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("users-students/$uid")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser=snapshot.getValue(studentData::class.java)
                Log.d("Latest","currentuser :${currentUser?.username}")
            }

        })
    }

    fun verifyUser()
    {
        if(auth.currentUser==null)
        {
            val intent= Intent(this, MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId)
        {
            R.id.signOut ->{
                auth.signOut()
                val intent= Intent(this,
                    MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }

            R.id.newTransaction ->{
                val intent= Intent(this, NewTransactionActivity::class.java)
                startActivity(intent)

            }

            R.id.scanQR ->{
                val intent= Intent(this, ScanningActivity::class.java)
                startActivity(intent)

            }
        }


        return super.onOptionsItemSelected(item)
    }


}
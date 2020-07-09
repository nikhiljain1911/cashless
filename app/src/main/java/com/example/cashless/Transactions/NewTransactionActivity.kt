package com.example.cashless.Transactions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.cashless.Constants
import com.example.cashless.DataService.merchantData
import com.example.cashless.R
import com.example.cashless.DataService.studentData
import com.example.cashless.DataService.userItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_transaction.*

class NewTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        supportActionBar?.title = "Select Shop"
        fetchUsers()

    }

        private fun fetchUsers()
        {
            val adapter=GroupAdapter<ViewHolder>()
            val ref= FirebaseDatabase.getInstance().getReference("users-merchants")
            ref.addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val user=it.getValue(merchantData::class.java)
                        if(user!=null)
                            adapter.add(userItem(user))


                    }
                    adapter.setOnItemClickListener{item, view ->
                        val useritem=item as userItem
                        val intent= Intent(view.context,TransactionLogActivity::class.java)
                        intent.putExtra(Constants.USER_KEY,useritem.user)
                        startActivity(intent)
                        finish()  //to finish new message activity

                    }
                    newTransactionRV.adapter=adapter

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
}

package com.example.cashless

import com.example.cashless.DataService.merchantData
import com.example.cashless.DataService.transactionMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_transactions_row.view.*

class LatestTransactionRow(val transactionmessage: transactionMessage): Item<ViewHolder>() {

    var user:merchantData?=null


    override fun getLayout(): Int {

        return R.layout.latest_transactions_row

    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.latestMessageRowMessage.text=transactionmessage.text

        val chatPartnerId:String
        if(transactionmessage.fromId== FirebaseAuth.getInstance().uid)
        {
            chatPartnerId=transactionmessage.toId
        }
        else
        {
            chatPartnerId=transactionmessage.fromId
        }

        val ref= FirebaseDatabase.getInstance().getReference("users-merchants/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                user=snapshot.getValue(merchantData::class.java)
                if(user!=null) {
                    viewHolder.itemView.latestMessageRowUsername.text = user?.username
                    Picasso.get().load(user?.profileImageUrl).into(viewHolder.itemView.latestMessageRowImage)
                }
            }

        })
    }

}
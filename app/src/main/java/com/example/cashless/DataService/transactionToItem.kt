package com.example.cashless.DataService

import com.example.cashless.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.transaction_to_row.view.*

class transactionToItem (val text:String, val user: merchantData): Item<ViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.transaction_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView2.text="Recieved:\nRs.$text"
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView2)
    }

}
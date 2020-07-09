package com.example.cashless.DataService

import com.example.cashless.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.transaction_from_row.view.*

class transactionFromItem (val text:String, val user: studentData): Item<ViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.transaction_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView.text="Paid:\nRs.$text"
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView)

    }

}
package com.example.cashless.DataService

import com.example.cashless.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.new_user_row.view.*


class userItem(val user: merchantData): Item<ViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.new_user_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.newUserUsername.text=user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.newUserImage)
    }

}
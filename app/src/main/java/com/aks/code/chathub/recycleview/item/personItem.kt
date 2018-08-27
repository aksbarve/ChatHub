package com.aks.code.chathub.recycleview.item

import android.content.Context
import com.aks.code.chathub.R
import com.aks.code.chathub.glide.GlideApp
import com.aks.code.chathub.model.User
import com.aks.code.chathub.util.Storage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.*

class personItem(val person: User,
                 val userId: String,
                 private val context: Context)
    :Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_chatName.text = person.name
        viewHolder.textView_status.text = person.status
        if(person.profilePic != null)
            GlideApp.with(context)
                    .load(Storage.pathtoReference(person.profilePic))
                    .placeholder(R.drawable.ic_info_outline_black_24dp)
                    .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.item_person
    }

package com.aks.code.chathub.recycleview.item

import android.view.Gravity
import android.widget.FrameLayout
import com.aks.code.chathub.R
import com.aks.code.chathub.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_chatmessage.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

abstract class MessageItem(private val message: Message)
    :Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTime(viewHolder)
        setMessageSide(viewHolder)
    }

    private fun setTime(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.textView_chat_time.text = dateFormat.format(message.time)
    }

    private fun setMessageSide(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.rootMessage.apply {
                backgroundResource = R.drawable.round_chat_boxwhite
                val lParam = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParam
            }
        } else {
            viewHolder.rootMessage.apply {
                backgroundResource = R.drawable.round_chat_box
                val lParam = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParam
            }
        }
    }


}
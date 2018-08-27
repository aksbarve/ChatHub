package com.aks.code.chathub.recycleview.item

import android.content.Context
import com.aks.code.chathub.R
import com.aks.code.chathub.model.ChatMessage
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_chatmessage.*


class ChatMessageItem (val message: ChatMessage,
                       val context: Context)
    :MessageItem(message) {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_chat_text.text = message.text
        super.bind(viewHolder, position)
    }


    override fun getLayout() = R.layout.item_chatmessage

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean{
        if (other !is ChatMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ChatMessageItem)

    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}



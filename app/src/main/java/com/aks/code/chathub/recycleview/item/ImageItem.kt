package com.aks.code.chathub.recycleview.item

import android.content.Context
import com.aks.code.chathub.R
import com.aks.code.chathub.glide.GlideApp
import com.aks.code.chathub.model.ImageMessage
import com.aks.code.chathub.util.Storage
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.image_resource.*

class ImageItem (val message: ImageMessage,
                 val context: Context)
    :MessageItem(message){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
        GlideApp.with(context)
                .load(Storage.pathtoReference(message.imagePath))
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .into(viewHolder.imageView_imageMessage)

    }

    override fun getLayout() = R.layout.image_resource

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean{
        if (other !is ImageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageItem)

    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }


}
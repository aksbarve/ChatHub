package com.aks.code.chathub.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aks.code.chathub.R
import com.aks.code.chathub.Sign_In
import com.aks.code.chathub.glide.GlideApp
import com.aks.code.chathub.util.FirestoreUtil
import com.aks.code.chathub.util.Storage
import com.aks.code.chathub.model.User
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_acc.*
import kotlinx.android.synthetic.main.fragment_acc.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


class AccFragment : Fragment() {

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImgByte: ByteArray
    private var pictureChanged = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_acc, container, false)

        view.apply {
            imageView.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
                }
                startActivityForResult(Intent.createChooser(intent,"Select Image for Profile"),RC_SELECT_IMAGE)
            }
            saveButton.setOnClickListener{
                if(::selectedImgByte.isInitialized)
                    Storage.uploadProfPic(selectedImgByte){imagePath ->
                        FirestoreUtil.updateUser(editName.text.toString(),
                                editStatus.text.toString(),imagePath)
                    }
                else
                    FirestoreUtil.updateUser(editName.text.toString(),
                            editStatus.text.toString(),null)
                toast("Saving changes")
            }

            signOffButton.setOnClickListener {
                AuthUI.getInstance()
                        .signOut(this@AccFragment.context!!)
                        .addOnCompleteListener {
                            startActivity(intentFor<Sign_In>().newTask().clearTask())
                        }
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null){
            val selectedImgPath = data.data
            val selectedImgBmp = MediaStore.Images.Media
                    .getBitmap(activity?.contentResolver, selectedImgPath)

            val outputStream = ByteArrayOutputStream()
            selectedImgBmp.compress(Bitmap.CompressFormat.JPEG,90,outputStream)
            selectedImgByte = outputStream.toByteArray()

            GlideApp.with(this)
                    .load(selectedImgByte)
                    .into(imageView)

            pictureChanged = true
        }
    }


    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser{user->
            if (this@AccFragment.isVisible){
                editName.setText(user.name)
                editStatus.setText(user.status)
               if(!pictureChanged && user.profilePic != null)
                    GlideApp.with(this)
                            .load(Storage.pathtoReference(user.profilePic))
                            .placeholder(R.drawable.ic_info_outline_black_24dp)
                            .into(imageView)

            }

        }
    }

}

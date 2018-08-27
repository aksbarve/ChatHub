package com.aks.code.chathub.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


object Storage {
    private val storageInst: FirebaseStorage by lazy {FirebaseStorage.getInstance()}

    private val curUserRef: StorageReference
         get() = storageInst.reference
            .child(FirebaseAuth.getInstance().currentUser?.uid
                    ?:throw NullPointerException("UID is null"))

    fun uploadProfPic(imageBytes: ByteArray,
                      onSuccess:(imagePath:String)->Unit){
        val ref = curUserRef.child("profilePicture/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes)
                .addOnSuccessListener {
                    onSuccess(ref.path)
                }
    }

    fun uploadChatImage(imageBytes: ByteArray,
                      onSuccess:(imagePath: String)->Unit){
        val ref = curUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes)
                .addOnSuccessListener {
                    onSuccess(ref.path)
                }
    }

    fun pathtoReference(path: String) = storageInst.getReference(path)
}
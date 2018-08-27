package com.aks.code.chathub.util

import android.content.Context
import android.util.Log
import com.aks.code.chathub.model.*
import com.aks.code.chathub.recycleview.item.ChatMessageItem
import com.aks.code.chathub.recycleview.item.ImageItem
import com.aks.code.chathub.recycleview.item.personItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

object FirestoreUtil {
    private val fireInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currUserDoc: DocumentReference
        get() = fireInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null")}")


    private val chatChannelRef = fireInstance.collection("chatChannels")


    fun currUserFirst(onComplete: () -> Unit) {
        currUserDoc.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = com.aks.code.chathub.model.User(FirebaseAuth.getInstance().currentUser?.displayName
                        ?: "",
                        "", null)
                currUserDoc.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }

    fun updateUser(name: String = "", status: String = "", profilePic: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (status.isNotBlank()) userFieldMap["status"] = status
        if (profilePic != null)
            userFieldMap["profilePic"] = profilePic
        currUserDoc.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (com.aks.code.chathub.model.User) -> Unit) {
        currUserDoc.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(com.aks.code.chathub.model.User::class.java)!!)
                }
    }

    fun addUserListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return fireInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "User Listener Error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(personItem(it.toObject(com.aks.code.chathub.model.User::class.java)!!, it.id, context))
                    }
                    onListen(items)
                }
    }

    fun removeListner(registration: ListenerRegistration) = registration.remove()

    fun getCreateChannel(otherUserId: String,
                         onComplete: (channelId: String) -> Unit) {

        currUserDoc.collection("engagedChat")
                .document(otherUserId).get().addOnSuccessListener {
                    if (it.exists()) {
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }

                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                    val newChannel = chatChannelRef.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))


                    currUserDoc
                            .collection("engagedChat")
                            .document(otherUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    fireInstance.collection("users").document(otherUserId)
                            .collection("engagedChat")
                            .document(currentUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    onComplete(newChannel.id)

                }

    }

    fun addChatListener(channelId: String, context: Context,
                        onListen: (List<Item>) -> Unit): ListenerRegistration {
        return chatChannelRef.document(channelId).collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", "ChatMessagesListener error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()
                    querySnapshot!!.documents.forEach {
                        if (it["type"] == MessageType.TEXT)
                            items.add(ChatMessageItem(it.toObject(ChatMessage::class.java)!!, context))
                        else
                            items.add(ImageItem(it.toObject(ImageMessage::class.java)!!, context))
                        return@forEach
                    }

                    onListen(items)
                }
    }

    fun sendChat(message: Message , channelId: String) {
        chatChannelRef.document(channelId)
                .collection("messages")
                .add(message)
    }
    }



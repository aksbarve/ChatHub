package com.aks.code.chathub.model

import java.util.*


data class ChatMessage (val text: String,
                        override val time: Date,
                        override val senderId: String,
                        override val type: String = MessageType.TEXT)
    :Message{
    constructor() : this("", Date(0),"")
}
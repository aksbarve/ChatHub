package com.aks.code.chathub.model

data class ChatChannel(val userIds: MutableList<String>) {
        constructor(): this(mutableListOf())
}
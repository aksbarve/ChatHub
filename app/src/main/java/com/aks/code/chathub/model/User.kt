package com.aks.code.chathub.model

data class User (val name: String,
                 val status: String,
                 val profilePic: String?){
    constructor():this(
            "",
            "",
            null
    )
                 }
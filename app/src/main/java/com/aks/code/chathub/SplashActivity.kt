package com.aks.code.chathub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            if(FirebaseAuth.getInstance().currentUser == null)
                startActivity<Sign_In>()
            else
                startActivity<MainActivity>()
            finish()
        }
    }

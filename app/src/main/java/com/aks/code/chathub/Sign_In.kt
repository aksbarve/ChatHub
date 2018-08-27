package com.aks.code.chathub

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aks.code.chathub.util.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign__in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class Sign_In : AppCompatActivity() {

    private val RC_SIGNIN = 1
    private val signInProviders = listOf(AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true).setRequireName(true).build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign__in)

        acc_signin.setOnClickListener{
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(signInProviders).setLogo(R.drawable.ic_text_icon)
                    .build()
            startActivityForResult(intent,RC_SIGNIN)
        }
    }

    override fun onActivityResult(requestCode: Int,resultCode:Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

        if(requestCode == RC_SIGNIN){
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Opening your inbox")

                FirestoreUtil.currUserFirst {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                    progressDialog.dismiss()
                }



            }
            else if(resultCode == Activity.RESULT_CANCELED){
                if(response == null) return

                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK -> longSnackbar(layout_details,"Network not available")
                    ErrorCodes.UNKNOWN_ERROR -> longSnackbar(layout_details,"Unknown error")
                }


            }
        }
    }
}

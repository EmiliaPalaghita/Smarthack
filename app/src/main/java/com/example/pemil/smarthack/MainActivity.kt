package com.example.pemil.smarthack

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pemil.smarthack.DataSource.UserDataSource
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 123
        const val ANONYMOUS = "nada"
    }

    private var mFireBaseAuth: FirebaseAuth? = null
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mUsersDatabasReference: DatabaseReference? = null
    private val userDataSource: UserDataSource = UserDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Uncomment this only when you want to populate database with new entries
         */
//        Parser.parseExchanges(this);

        mFireBaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        mUsersDatabasReference = mFirebaseDatabase?.reference?.child("users")

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                //user is signed in
                onSignedInInitialized(user.displayName)
                userDataSource.sendUserToDB(userDataSource.createNewUser(user))
            } else {
                //user is signed out
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                            Arrays.asList<AuthUI.IdpConfig>(
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )

            }
        }
    }

    private fun onSignedInInitialized(displayName: String?) {
//        username.text = displayName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mFireBaseAuth?.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        if (mFireBaseAuth != null) {
            mFireBaseAuth?.removeAuthStateListener(mAuthStateListener)
        }
    }
}

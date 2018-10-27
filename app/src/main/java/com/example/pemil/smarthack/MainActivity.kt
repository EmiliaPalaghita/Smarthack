package com.example.pemil.smarthack

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
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

    var currentUser: String? = ANONYMOUS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFireBaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        mUsersDatabasReference = mFirebaseDatabase?.reference?.child("users")

//        mUsersDatabasReference?.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
//                if (dataSnapshot.exists()) {
//                    //user already in database
//                } else {
//                    //TODO - create user for database
//                    mUsersDatabasReference?.push()?.setValue(currentUser)
//                }
//            }
//
//            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
//
//            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
//
//            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                //user is signed in
                onSignedInInitialized(user.displayName)
//                mUsersDatabasReference?.push()?.setValue()
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
        username.text = displayName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
                currentUser = mFireBaseAuth?.currentUser?.uid
                mUsersDatabasReference?.push()?.setValue(currentUser)
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

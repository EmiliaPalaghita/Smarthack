package com.example.pemil.smarthack

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pemil.smarthack.DataSource.UserDataSource
import com.example.pemil.smarthack.ui.home.HomeFragment
import com.example.pemil.smarthack.ui.home.MyInvestmentFragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.R.attr.fragment
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import com.example.pemil.smarthack.R.id.navigationView
import android.R
import android.support.annotation.NonNull
import android.view.MenuItem


class MainActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 123
    }

    private var mFireBaseAuth: FirebaseAuth? = null
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mUsersDatabasReference: DatabaseReference? = null
    private val userDataSource: UserDataSource = UserDataSource()
    private lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Uncomment this only when you want to populate database for investments with new entries
         */
//        Parser.parseExchanges(this);

        mFireBaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        mUsersDatabasReference = mFirebaseDatabase?.reference?.child("users")

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                //user is signed in
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

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)

//        bottomNavigation.setOnNavigationItemSelectedListener(
//            object : BottomNavigationView.OnNavigationItemSelectedListener {
//                override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                    when (item.getItemId()) {
//
//                    }
//                    return true
//                }
//            })

//        home_button.setOnClickListener {
//            val homeFragment = HomeFragment()
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, homeFragment)
//                    .commit()
//        }
//
//        my_investments_button.setOnClickListener {
//            //TODO - open MyInvestmentsFragment
//            /*
//            * val myInvestmentFragment = MyInvestmentFragment()
//            supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, myInvestmentFragment)
//            .commit()
//            *
//            * */
//        }
//
//        new_investments_button.setOnClickListener {
//            //TODO - open NewInvestmentFragment
//            /*
//            * val newInvestmentFragment = NewInvestmentFragment()
//            supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, newInvestmentFragment)
//            .commit()
//            *
//            * */
//        }
//
//        profile_button.setOnClickListener {
//            //TODO - open ProfileFragment
//            /*
//            * val profileFragment = ProfileFragment()
//            supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, profileFragment)
//            .commit()
//            *
//            * */
//        }
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

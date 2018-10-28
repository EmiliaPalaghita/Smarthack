package com.example.pemil.smarthack.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pemil.smarthack.DataSource.UserDataSource
import com.example.pemil.smarthack.Models.User
import com.example.pemil.smarthack.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profile_screen.*
import java.lang.Long
import java.util.*

class ProfileFragment : Fragment() {

    internal var thisUser: User? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usrDataSource = UserDataSource()
        val user = usrDataSource.auth.currentUser

        usrDataSource.table.child(user!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                thisUser = dataSnapshot.getValue(User::class.java)
                edit_name.setText(thisUser?.name)
                user_profile_name.text = thisUser?.name
                edit_email.setText(thisUser?.email)
                edit_telephone.setText(thisUser?.telephone.toString())
                if (thisUser?.telephone == null)
                    edit_telephone.setText("")
                else edit_telephone.setText(thisUser?.telephone.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        edit_save_button.setOnClickListener {
            val userUpdate = HashMap<String, Any>()
            userUpdate[user.uid] = User(edit_name.text.toString(),
                    edit_country.text.toString(), null, if (edit_telephone.text != null)
                Long.getLong(edit_telephone.text.toString())
            else
                null,
                    edit_email.text.toString(), user.uid, null, null)
            FirebaseDatabase.getInstance().reference.child("users").updateChildren(userUpdate)
            user_profile_name.text = edit_name.text
            edit_save_button.visibility = View.GONE
            edit_name.isEnabled = false
            edit_telephone.isEnabled = false
            edit_email.isEnabled = false
            edit_country.isEnabled = false

            Toast.makeText(this.context, "Profile updated", Toast.LENGTH_LONG).show()
        }

        editButton.setOnClickListener {
            edit_name.isEnabled = true
            edit_telephone.isEnabled = true
            edit_email.isEnabled = true
            edit_country.isEnabled = true
            edit_save_button.visibility = View.VISIBLE
        }

        edit_interests_button.setOnClickListener {
            val intent = Intent(this.context, InterestsActivity::class.java)
            startActivity(intent)
        }
    }
}
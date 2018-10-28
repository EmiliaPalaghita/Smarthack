package com.example.pemil.smarthack.DataSource;

import android.util.Log;
import com.example.pemil.smarthack.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class UserDataSource {
    private DatabaseReference table;
    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    public static User thisUser;

    public UserDataSource() {
        this.auth = FirebaseAuth.getInstance();
        this.dataBase = FirebaseDatabase.getInstance();
        this.table = dataBase.getReference().child("users");
    }

    public void sendUserToDB(User user) {
        this.table.child(user.getId()).setValue(user);
        Log.d("DATABASE USER", user.toString());
    }

    public void getCurrentUserFromDB() {
        final FirebaseUser user = this.auth.getCurrentUser();
        table.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public DatabaseReference getTable() {
        return this.table;
    }

    public FirebaseAuth getAuth() {
        return this.auth;
    }

    public FirebaseDatabase getDataBase() {
        return this.dataBase;
    }

    public User createNewUser(FirebaseUser currentUser) {
        return new User(
                currentUser.getDisplayName(),
                null,
                null,
                null,
                currentUser.getEmail(),
                currentUser.getUid(),
                null,
                null
        );
    }
}

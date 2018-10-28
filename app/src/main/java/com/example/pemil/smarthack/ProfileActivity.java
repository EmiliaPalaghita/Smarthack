package com.example.pemil.smarthack;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pemil.smarthack.DataSource.UserDataSource;
import com.example.pemil.smarthack.Models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends Activity {
    static User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        final EditText name = findViewById(R.id.edit_name);
        final EditText telephone = findViewById(R.id.edit_telephone);
        final EditText country = findViewById(R.id.edit_country);
        final EditText email = findViewById(R.id.edit_email);
        final Button saveButton = findViewById(R.id.edit_save_button);
        final TextView displayedName = findViewById(R.id.user_profile_name);

        final UserDataSource usrDataSource = new UserDataSource();
        final FirebaseUser user = usrDataSource.getAuth().getCurrentUser();

        usrDataSource.getTable().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(User.class);
                name.setText(thisUser.getName());
                displayedName.setText(thisUser.getName());
                email.setText(thisUser.getEmail());
                telephone.setText(String.valueOf(thisUser.getTelephone()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

        findViewById(R.id.edit_save_button).setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                Map<String, Object> userUpdate = new HashMap<>();
                userUpdate.put(user.getUid(), new User(name.getText().toString(),
                        country.getText().toString(), null, telephone.getText() != null ?
                        Long.getLong(telephone.getText().toString()) : null,
                        email.getText().toString(), user.getUid(), null,null ));
                FirebaseDatabase.getInstance().getReference().child("users").updateChildren(userUpdate);
                displayedName.setText(name.getText());
                saveButton.setVisibility(View.GONE);
                name.setEnabled(false);
                telephone.setEnabled(false);
                email.setEnabled(false);
                country.setEnabled(false);

                Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                name.setEnabled(true);
                telephone.setEnabled(true);
                email.setEnabled(true);
                country.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
            }
        });
    }
}

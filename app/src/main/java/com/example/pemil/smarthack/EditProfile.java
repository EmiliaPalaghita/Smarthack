package com.example.pemil.smarthack;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                EditText first_name = findViewById(R.id.edit_first_name);
                EditText surname = findViewById(R.id.edit_surname);
                EditText email = findViewById(R.id.edit_email);
                Button saveButton = findViewById(R.id.edit_save_button);

                first_name.setEnabled(true);
                surname.setEnabled(true);
                email.setEnabled(true);
                saveButton.setVisibility(View.VISIBLE);
            }
        });
    }


}

package com.example.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {
    TextView fullname1,username1;
    TextInputLayout fullname2,email,phone,password;
//    ImageView editBtnForName,editBtnForPassword;
    DatabaseReference reference;
    Button logoutBtn;
    ImageView threeDot;
    String user_username,user_name,user_email,user_phone,user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        fullname1=findViewById(R.id.fullname1);
        username1=findViewById(R.id.username1);
        fullname2=findViewById(R.id.fullname2);
        email=findViewById(R.id.useremail);
        phone=findViewById(R.id.userphone);
        password=findViewById(R.id.userpassword);
        threeDot=findViewById(R.id.threeDot);
        logoutBtn=findViewById(R.id.logoutBtn);
        reference= FirebaseDatabase.getInstance("https://magical-9c8b8-default-rtdb.firebaseio.com/").getReference().child("users");

        showAllData();

        threeDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeDot.setVisibility(View.GONE);
                logoutBtn.setVisibility(View.VISIBLE);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        editBtnForName=findViewById(R.id.editNameBtn);
//        editBtnForPassword=findViewById(R.id.editPasswordBtn);



//        editBtnForName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fullname2.getEditText().setFocusable(false);
//            }
//        });
//        editBtnForPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                password.getEditText().requestFocus();
//            }
//        });

    }

    private void showAllData() {
        Intent intent=getIntent();
         user_username=intent.getStringExtra("username");
         user_name=intent.getStringExtra("name");
         user_email=intent.getStringExtra("email");
         user_phone=intent.getStringExtra("phone");
         user_password=intent.getStringExtra("password");

        fullname1.setText(user_name);
        username1.setText(user_username);
        fullname2.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phone.getEditText().setText(user_phone);
        password.getEditText().setText(user_password);

    }

    public void updateDetails(View view){
        if (nameIsChanged() || passwordIsChanged()){
//            fullname2.getEditText().setFocusable(false);
//            password.getEditText().setFocusable(false);

            Toast.makeText(this, "Your data is updated", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Data is same and can't be update", Toast.LENGTH_LONG).show();
        }
    }

    private boolean nameIsChanged() {
        String s=fullname2.getEditText().getText().toString();
        if (!user_name.equals(s)){
            reference.child(user_username).child("name").setValue(s);
            fullname1.setText(s);
            return true;
        }
        else{
            return false;
        }
    };

    private boolean passwordIsChanged() {
        String s=password.getEditText().getText().toString();
        if (!user_password.equals(s)){
            reference.child(user_username).child("password").setValue(s);
            return true;
        }
        else{
            return false;
        }
    };
}
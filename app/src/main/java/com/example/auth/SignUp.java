package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    TextInputLayout regName,regUserName,regEmail,regPhone,regPassword;
    Button regBtn, login;
    LinearLayout loadingScreen;
    ProgressBar progressBar;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        regBtn=findViewById(R.id.registerBtn);
        login=findViewById(R.id.log_in_btn);
        regName=findViewById(R.id.fullname);
        regUserName=findViewById(R.id.username);
        regEmail=findViewById(R.id.email);
        regPhone=findViewById(R.id.phone);
        regPassword=findViewById(R.id.password);
        loadingScreen=findViewById(R.id.loadingScreenSignIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingScreen.setVisibility(View.VISIBLE);
                if (!validateName()| !validateUserName() | !validateEmail() | !validatePhone() | !validatePassword()){
                    loadingScreen.setVisibility(View.GONE);
                    return;
                }
                reference=FirebaseDatabase.getInstance("https://magical-9c8b8-default-rtdb.firebaseio.com/").getReference().child("users");

                String name=regName.getEditText().getText().toString().trim();
                String username=regUserName.getEditText().getText().toString().trim();
                String email=regEmail.getEditText().getText().toString().trim();
                String phone=regPhone.getEditText().getText().toString().trim();
                String password=regPassword.getEditText().getText().toString();

                Query checkQuery=reference.orderByChild("username").equalTo(username);
                regUserName.setError(null);
                regPhone.setError(null);
                regEmail.setError(null);
                regUserName.setErrorEnabled(false);
                regPhone.setErrorEnabled(false);
                regEmail.setErrorEnabled(false);
                checkQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            regUserName.setError("username already exist! Try another");
                            loadingScreen.setVisibility(View.GONE);
                            return;
                        }

                        else {
                            Query checkQueryForEmail=reference.orderByChild("email").equalTo(email);
                            checkQueryForEmail.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2.exists()){
                                        regEmail.setError("Email already exist! Try another");
                                        loadingScreen.setVisibility(View.GONE);
                                        return;
                                    }

                                    else {
                                        Query checkQueryForPhone=reference.orderByChild("phone").equalTo(phone);
                                        regPhone.setErrorEnabled(false);
                                        checkQueryForPhone.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                                if (dataSnapshot3.exists()){
                                                    regPhone.setError("phone no already registered");
                                                    loadingScreen.setVisibility(View.GONE);
                                                    return;
                                                }
                                                else{
                                                    UserHelperClass helperClass=new UserHelperClass(name,username,email,phone,password);
                                                    reference.child(username).setValue(helperClass);

                                                    Intent intentForVerify=new Intent(getApplicationContext(),PhoneOtp.class);
                                                    intentForVerify.putExtra("phoneNo",phone);
                                                    intentForVerify.putExtra("name",name);
                                                    intentForVerify.putExtra("password",password);
                                                    intentForVerify.putExtra("email",email);
                                                    intentForVerify.putExtra("username",username);
                                                    startActivity(intentForVerify);
                                                    finish();

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//                Intent intentForVerify=new Intent(getApplicationContext(),PhoneOtp.class);
//                intentForVerify.putExtra("phoneNo",phone);
//                intentForVerify.putExtra("name",name);
//                intentForVerify.putExtra("password",password);
//                intentForVerify.putExtra("email",email);
//                intentForVerify.putExtra("username",username);
//                startActivity(intentForVerify);

//                UserHelperClass helperClass=new UserHelperClass(name,username,email,phone,password);
//                reference.child(username).setValue(helperClass);
//
////
//                Intent intent=new Intent(SignUp.this,UserProfile.class);
//                intent.putExtra("name",name);
//                intent.putExtra("password",password);
//                intent.putExtra("email",email);
//                intent.putExtra("phone",phone);
//                intent.putExtra("username",username);
//                startActivity(intent);
            }

            private Boolean validateName(){
                String name=regName.getEditText().getText().toString();
                if (name.isEmpty()){
                    regName.setError("Feild can't be empty!");
                    regName.setErrorEnabled(true);
                    return false;
                }
                else {
                    regName.setError(null);
                    regName.setErrorEnabled(false);
                    return true;
                }
            }
            private Boolean validateUserName(){
                String username=regUserName.getEditText().getText().toString();
                String nowhiteSpace="\\A\\w{4,20}\\z";
                if (username.isEmpty()){
                    regUserName.setError("Feild can't be empty!");
                    return false;
                }
                else if (username.length()>=15){
                    regUserName.setError("Username is too long");
                    return false;
                }
                else if (!username.matches(nowhiteSpace)){
                    regUserName.setError("No whitespaces allowed");
                    return false;
                }
                else {
                    regUserName.setError(null);
                    regUserName.setErrorEnabled(false);
                    return true;
                }
            }

            private Boolean validateEmail(){
                String email=regEmail.getEditText().getText().toString();
                String emailPattern="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
                if (email.isEmpty()){
                    regEmail.setError("Feild can't be empty!");
                    return false;
                }
                else if (!email.matches(emailPattern)){
                    regEmail.setError("Invalid Email Address");
                    return false;
                }
                else {
                    regEmail.setError(null);
                    regEmail.setErrorEnabled(false);
                    return true;
                }
            }
            private Boolean validatePhone(){
                String phone=regPhone.getEditText().getText().toString();
                if (phone.isEmpty()){
                    regPhone.setError("Feild can't be empty!");
                    return false;
                }
                else {
                    regPhone.setError(null);
                    regPhone.setErrorEnabled(false);
                    return true;
                }
            }
            private Boolean validatePassword(){
                String password=regPassword.getEditText().getText().toString();
                String passwordPattern="^" +
                //" (?=.*[0-9])" + //at least 1 digit
                //" (?=.*[a-z])" + //at least 1 lower case letter
                //" (?=.*[A-Z])" //at least 1 upper case letter
                "(?=.*[a-zA-Z])" + //any letter
                 "(?=.*[@#$%^&+=])" + //at least 1 special character
                 "(?=\\S+$)" +//no white spaces
                 ".{4,}" +  //at least 4 characters
                "$";
                
                if (password.isEmpty()){
                    regPassword.setError("Feild can't be empty!");
                    return false;
                }
                else if (!password.matches(passwordPattern)){
                    regPassword.setError("Password is too weak");
                    return false;
                }
                else {
                    regPassword.setError(null);
                    regPassword.setErrorEnabled(false);
                    return true;
                }
            }
        });


    }



}
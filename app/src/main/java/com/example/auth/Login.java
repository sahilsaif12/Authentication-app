package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.util.Pair;


public class Login extends AppCompatActivity {
    Button signup,submit;
    TextInputLayout username,password;
    TextView heading,subtext;
    ImageView img;
    LinearLayout loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        heading=findViewById(R.id.logo_text);
        subtext=findViewById(R.id.subtext);
        img=findViewById(R.id.logo_img);
        signup=findViewById(R.id.log_in_btn);
        submit=findViewById(R.id.registerBtn);
        loadingScreen=findViewById(R.id.loadingScreenLogIn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingScreen.setVisibility(View.VISIBLE);
                if (!validateUserName() | !validatePassword()){
                    loadingScreen.setVisibility(View.GONE);
                    return;
                }
                else {
                    final String userEnteredUserName=username.getEditText().getText().toString().trim();
                    final String userEnteredPassword=password.getEditText().getText().toString().trim();

                    DatabaseReference reference= FirebaseDatabase.getInstance("https://magical-9c8b8-default-rtdb.firebaseio.com/").getReference().child("users");
                    Query checkuser=reference.orderByChild("username").equalTo(userEnteredUserName);

                    checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                username.setError(null);
                                username.setErrorEnabled(false);
                                String passwordFromDb=dataSnapshot.child(userEnteredUserName).child("password").getValue(String.class);
                                if (passwordFromDb.equals(userEnteredPassword)){
                                    String nameFromDb=dataSnapshot.child(userEnteredUserName).child("name").getValue(String.class);
                                    String usernameFromDb=dataSnapshot.child(userEnteredUserName).child("username").getValue(String.class);
                                    String emailFromDb=dataSnapshot.child(userEnteredUserName).child("email").getValue(String.class);
                                    String phoneFromDb=dataSnapshot.child(userEnteredUserName).child("phone").getValue(String.class);

                                    Intent intent=new Intent(Login.this,UserProfile.class);
                                    intent.putExtra("name",nameFromDb);
                                    intent.putExtra("password",passwordFromDb);
                                    intent.putExtra("email",emailFromDb);
                                    intent.putExtra("phone",phoneFromDb);
                                    intent.putExtra("username",usernameFromDb);
                                    startActivity(intent);
                                }
                                else {
                                    password.setError("Wrong password");
                                    loadingScreen.setVisibility(View.GONE);
                                    password.requestFocus();
                                }
                                loadingScreen.setVisibility(View.GONE);


                            }
                            else {
                                username.setError("User not exist");
                                loadingScreen.setVisibility(View.GONE);
                                username.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            private Boolean validateUserName() {
                String user = username.getEditText().getText().toString();
                if (user.isEmpty()) {
                    username.setError("Feild can't be empty!");
                    return false;
                }
                else {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    return true;
                }

            }
            private Boolean validatePassword(){
                String p=password.getEditText().getText().toString();

                if (p.isEmpty()){
                    password.setError("Feild can't be empty!");
                    return false;
                }
                else {
                    password.setError(null);
                    password.setErrorEnabled(false);
                    return true;
                }
            }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignUp.class);
                Pair[] pairs=new Pair[7];
                pairs[0]=new Pair<View,String>(img,"logo_img");
                pairs[1]=new Pair<View,String>(heading,"logo_text");
                pairs[2]=new Pair<View,String>(subtext,"bottomText");
                pairs[3]=new Pair<View,String>(username,"username");
                pairs[4]=new Pair<View,String>(password,"password");
                pairs[5]=new Pair<View,String>(submit,"button1");
                pairs[6]=new Pair<View,String>(signup,"button2");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);

                    startActivity(intent,options.toBundle());
                }
            }
        });
    }

}
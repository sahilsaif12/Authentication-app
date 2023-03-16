package com.example.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneOtp extends AppCompatActivity {
    Button verifyBtn;
    TextInputLayout otpText;
    ProgressBar progressBar;
    String verficationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otp);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        verifyBtn=findViewById(R.id.verify_btn);
        otpText=findViewById(R.id.otptext);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        String phoneNo=getIntent().getStringExtra("phoneNo");

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpText.getEditText().getText().toString();
                if (otp.isEmpty() || otp.length()<6){
                    otpText.setError("Wrong OTP ");
                    otpText.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(otp);
            }
        });
        sendVerificationCodeToUser(phoneNo);
    }

    private void sendVerificationCodeToUser(String phoneNumber) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)             // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verficationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            // user entered code (works automatically on same mobile)
            String code= phoneAuthCredential.getSmsCode();
            if (code!=null){
                otpText.getEditText().setText(code);
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("first :",e.getMessage());
            Toast.makeText(PhoneOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verficationCodeBySystem,codeByUser);
        signInTheUSerByCredential(credential);
    }

    private void signInTheUSerByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String phone=getIntent().getStringExtra("phoneNo");
                    String name=getIntent().getStringExtra("name");
                    String username=getIntent().getStringExtra("username");
                    String email=getIntent().getStringExtra("email");
                    String password=getIntent().getStringExtra("password");

                    Intent intent=new Intent(PhoneOtp.this,UserProfile.class);
                    intent.putExtra("name",name);
                    intent.putExtra("password",password);
                    intent.putExtra("email",email);
                    intent.putExtra("phone",phone);
                    intent.putExtra("username",username);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Log.d("last :",task.getException().getMessage());
//                    Toast.makeText(PhoneOtp.this, "last :"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
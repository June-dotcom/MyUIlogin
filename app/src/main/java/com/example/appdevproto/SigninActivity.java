package com.example.appdevproto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaeger.library.StatusBarUtil;

public class SigninActivity extends AppCompatActivity {
    Button sign_up_btn, sign_in_btn;
    FirebaseAuth fAuth;
    TextInputEditText email_ed, password_ed;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null){
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        StatusBarUtil.setTranslucent(this);
        fAuth = FirebaseAuth.getInstance();
        sign_in_btn = findViewById(R.id.sign_in_btn);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        email_ed = (TextInputEditText)findViewById(R.id.sign_in_email_ed_txt);
        password_ed = (TextInputEditText)findViewById(R.id.sign_in_password_txt);
//        password_ed.setTransformationMethod(null);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String email_auth_str = email_ed.getText().toString().trim();
                String pass_auth_str = password_ed.getText().toString().trim();
                if (TextUtils.isEmpty(email_auth_str)){
                    email_ed.setError("Please enter your email");
                    return;
                }
                if (TextUtils.isEmpty(pass_auth_str)){
                    password_ed.setError("Please enter your password");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email_auth_str, pass_auth_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Snackbar.make(view, "Successful", Snackbar.LENGTH_LONG).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Snackbar.make(view, "Signed in failed", Snackbar.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
            }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninActivity.this, SignupActiviy.class));
            }
        });

    }

    public void updateUI(FirebaseUser user){
        if (user != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
}
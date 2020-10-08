package com.example.appdevproto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

public class SignupActiviy extends AppCompatActivity {
    Button sign_up_btn;
    FirebaseAuth mAuth;
    TextInputEditText name_ed, email_ed, password_ed, repeat_pass_ed;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        StatusBarUtil.setTranslucent(this);
        name_ed = (TextInputEditText) findViewById(R.id.sign_up_name_ed);
        email_ed = (TextInputEditText) findViewById(R.id.sign_up_email_ed);
        password_ed = (TextInputEditText) findViewById(R.id.sign_up_password_ed);
        repeat_pass_ed = (TextInputEditText) findViewById(R.id.sign_up_password_repeat_ed);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        mAuth = FirebaseAuth.getInstance();
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String name_str = name_ed.getText().toString();
                final String email_str = email_ed.getText().toString();
                String password_str = password_ed.getText().toString();
                String repeat_pass_str = repeat_pass_ed.getText().toString();
                if (TextUtils.isEmpty(name_str)) {
                    name_ed.setError("Please enter your name");
                    return;
                }
                if (TextUtils.isEmpty(email_str)) {
                    email_ed.setError("Please enter your email");
                    return;
                }
                if (!password_str.equals(repeat_pass_str)) {
                    repeat_pass_ed.setError("Passwords doesn\'t match");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email_str, password_str)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(view, "Verification email has been sent", Snackbar.LENGTH_LONG);
                                        }
                                    });
                                    Map<String, Object> user_info = new HashMap<>();
                                    user_info.put("fullname", name_str);


                                    db.collection("users").document(user.getUid())
                                            .set(user_info)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Snackbar.make(view, "Success info", Snackbar.LENGTH_LONG);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Snackbar.make(view, "Info failed", Snackbar.LENGTH_LONG);

                                                }
                                            });

                                    updateUI();
                                } else {
                                    Toast.makeText(SignupActiviy.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }
                        });
//                mAuth.createUserWithEmailAndPassword(email_str, password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Snackbar.make(view, "Verification Email has been sent", Snackbar.LENGTH_LONG).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Snackbar.make(view, "Verification email failed", Snackbar.LENGTH_LONG).show();
//                                }
//                            });
//
//                            userID = mAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fStore.collection("users").document(userID);
//                            Map<String, Object> user = new HashMap<>();
//                            user.put("Name", name_str);
//                            user.put("email", email_str);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
////                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
////                                    Log.d(TAG, "onFailure: " + e.toString());
//                                }
//                            });
//                        } else {
//                            Snackbar.make(view, "Error", Snackbar.LENGTH_LONG).show();
//                        }
//                    }
//                });

            }
        });


    }

    public void updateUI() {
        FirebaseAuth.getInstance().signOut();//logout
            startActivity(new Intent(getApplicationContext(), SigninActivity.class));

    }
}
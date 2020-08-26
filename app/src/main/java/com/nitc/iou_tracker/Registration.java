package com.nitc.iou_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {


    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword,editTextPassword2,fNmae,contact;
    private FirebaseAuth mAuth;

    String userId;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 =  findViewById(R.id.editTextPassword2);
        progressBar =  findViewById(R.id.progressbar);
        fNmae = findViewById(R.id.editText9);
        contact = findViewById(R.id.editText10);
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSame = new Intent(Registration.this,MainActivity.class);
                startActivity(toSame);
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cnfpassword = editTextPassword2.getText().toString().trim();
        String name = fNmae.getText().toString().trim();
        String phone = contact.getText().toString().trim();
        String expression = "^[\\w.+\\-]+@nitc\\.ac\\.in$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        String expression2 = "^[\\w.+\\-]+@gmail\\.com$";
        Pattern pattern1 = Pattern.compile(expression2, Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(inputStr);

        if(name.isEmpty()){
            fNmae.setError("Please Enter Name");
            fNmae.requestFocus();
        }else if(phone.isEmpty()){
            contact.setError("Please Enter contact number");
            contact.requestFocus();
        }else if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();

        } else if (!matcher.matches() && !matcher1.matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();

        } else if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();

        } else if (!password.equals(cnfpassword)) {
            editTextPassword.setError("Password mismatch");
            editTextPassword.requestFocus();

        } else if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();

        } else {

            progressBar.setVisibility(View.VISIBLE);


            mAuth.createUserWithEmailAndPassword(email.toLowerCase(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {


                        mAuth.getCurrentUser().sendEmailVerification();
                        Toast.makeText(getApplicationContext(), "Email Verification has been sent", Toast.LENGTH_LONG).show();


                            userId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userId);

                            Map<String, Object> user = new HashMap<>();
                            user.put("FName", name);
                            user.put("Email", email.toLowerCase());
                            user.put("Contact", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: User profile is created for " + userId);
                                }
                            });

                        startActivity(new Intent(Registration.this, MainActivity.class));
                        finish();

                        Toast.makeText(getApplicationContext(), "Email Verification has been sent", Toast.LENGTH_LONG).show();
                    } else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        }
    }
}

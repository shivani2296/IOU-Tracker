package com.nitc.iou_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    TextView signUp,forgotPassword;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        signUp = findViewById(R.id.textViewSignup);
        forgotPassword = findViewById(R.id.forgotPassword);

        login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Registration.class));

            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });
    }
        private void userLogin() {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();



            if (email.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }


            String expression = "^[\\w.+\\-]+@nitc\\.ac\\.in$";
            String expression2 = "^[\\w.+\\-]+@gmail\\.com$";
            CharSequence inputStr = email;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Pattern pattern1 = Pattern.compile(expression2, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            Matcher matcher1 = pattern1.matcher(inputStr);


            if (!matcher.matches() && !matcher1.matches()){
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
                return;
            }else if (password.isEmpty()) {
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
                return;
            }else if (password.length() < 6) {
                editTextPassword.setError("Password is not correct");
                editTextPassword.requestFocus();
                return;
            }else {

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (mAuth == null || !mAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(getApplicationContext(), "Verify email id first", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Logged In successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {

                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }

        @Override
        protected void onStart() {
            super.onStart();


            if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {

                finish();
                //Log.d("TAG", String.valueOf(mAuth.getCurrentUser()));

                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        }


}

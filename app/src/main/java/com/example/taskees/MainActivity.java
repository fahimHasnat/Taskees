package com.example.taskees;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private TextView signup;
    private EditText pass;
    private EditText email;
    private Button btnLogin;

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mDialog=new ProgressDialog(this);

        final Toast toast;

        signup = findViewById(R.id.signUp);
        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_btn);

        String first = "Don't have an account ? ";
        String next = "<font color='#EE0000'><u>Sign up here</u></font>";
        signup.setText(Html.fromHtml(first + next));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString();
                String mPass = pass.getText().toString();

                if(TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field");
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    Toast.makeText(getApplicationContext(),"Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(mPass)){
                    pass.setError("Required Field");
                    return;
                }

                mDialog.setMessage("Processing");
                mDialog.show();


                mAuth.signInWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(isNetworkConnected()){
                            if(task.isSuccessful()){
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                                mDialog.show();
                            } else{
                                Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());
                                Toast.makeText(getApplicationContext(),"Incorrect Email or Password",Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"No Connection", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
                finish();
            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

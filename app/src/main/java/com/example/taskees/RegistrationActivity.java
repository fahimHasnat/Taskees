package com.example.taskees;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText regName;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regRePassword;
    private Button regButton;

    private TextView Login_text;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);

        regName = findViewById(R.id.register_name);
        regEmail = findViewById(R.id.register_email);
        regPassword = findViewById(R.id.register_password);
        regRePassword = findViewById(R.id.register_repassword);

        regButton = findViewById(R.id.register_btn);

        Login_text = findViewById(R.id.logInTxt);

        String first = "Already have an account ? ";
        String next = "<font color='#EE0000'><u>Sign in here</u></font>";
        Login_text.setText(Html.fromHtml(first + next));

        Login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = regEmail.getText().toString().trim();
                String mPass = regPassword.getText().toString().trim();

                if(TextUtils.isEmpty(mEmail)){
                    regEmail.setError("Required Field");
                    return;
                }
                if(TextUtils.isEmpty(mPass)){
                    regPassword.setError("Required Field");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try{
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                mDialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Couldn't register, try again", Toast.LENGTH_LONG).show();
                                mDialog.dismiss();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}

package com.example.nutrifoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    TextView register, forgot;
    EditText login_email, login_password;
    Button btn_login;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.login_daftar);
        forgot = findViewById(R.id.login_forgot);
        login_email = findViewById(R.id.et_login_username);
        login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp_start = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUp_start);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lupaPass_start = new Intent(LoginActivity.this, LupaPassActivity.class);
                startActivity(lupaPass_start);
            }
        });

        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email = login_email.getText().toString();
                String txt_password = login_password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent toDashboard = new Intent(LoginActivity.this, DashboardActivity.class);
                                        toDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(toDashboard);
                                        finish();
                                        //pd.dismiss();
                                    } else{
                                        Toast.makeText(LoginActivity.this, "Authentification failed!", Toast.LENGTH_SHORT).show();
                                        //pd.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }
}

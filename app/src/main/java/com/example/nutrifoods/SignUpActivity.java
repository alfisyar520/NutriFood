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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    TextView login;
    EditText sign_email, sign_username, sign_password, sign_repassword;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = findViewById(R.id.regis_login);
        sign_email = findViewById(R.id.input_register_email);
        sign_username = findViewById(R.id.input_register_username);
        sign_password = findViewById(R.id.input_register_password);
        sign_repassword = findViewById(R.id.input_register_repassword);
        btn_register = findViewById(R.id.btn_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_start = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(login_start);
            }
        });

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = sign_username.getText().toString();
                String txt_email = sign_email.getText().toString();
                String txt_password = sign_password.getText().toString();
                String txt_repassword = sign_repassword.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(SignUpActivity.this, "All field are required", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length() < 6){
                    Toast.makeText(SignUpActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else if(!txt_password.equals(txt_repassword)){
                    Toast.makeText(SignUpActivity.this, "password and re-password is not match", Toast.LENGTH_SHORT).show();
                } else{
                    register(txt_username, txt_email, txt_password);
                    //pd.dismiss();
                }
            }
        });

    }



    private void register(final String username, String email, final String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", username);
                            hashMap.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/handycrafts-shop.appspot.com/o/default.png?alt=media&token=a721ee3d-b599-40fc-aab7-f58cadc15861");


                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Toast.makeText(SignUpActivity.this, "Register Anda Berhasil!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(SignUpActivity.this, "Register Anda Gagal!", Toast.LENGTH_SHORT).show();
                            //pd.dismiss();
                        }
                    }
                });
    }
}

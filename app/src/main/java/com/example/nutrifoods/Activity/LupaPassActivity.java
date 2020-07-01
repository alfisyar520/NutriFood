package com.example.nutrifoods.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nutrifoods.R;

public class LupaPassActivity extends AppCompatActivity {

    EditText et_username;
    Button btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        et_username = findViewById(R.id.lupaPass_username);
        btn_reset = findViewById(R.id.btn_lupaPass_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_start = new Intent(LupaPassActivity.this,LoginActivity.class);
                startActivity(login_start);
            }
        });
    }
}

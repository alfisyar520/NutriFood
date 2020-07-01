package com.example.nutrifoods.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nutrifoods.R;

public class HasilActivity extends AppCompatActivity {

    private TextView tv_nama_makanan;
    private ImageView iv_gambar_makanan;
    private String id_makanan, nama_makanan, userId, image, usernamePublisher,currentDate, currentTime;

    //firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        tv_nama_makanan = findViewById(R.id.tv_hasil_namaMakanan);
        iv_gambar_makanan = findViewById(R.id.iv_hasil_GambarMakanan);

        getIncomingIntent();

    }

    private void getIncomingIntent() {
        id_makanan = getIntent().getStringExtra("id_makanan");
        nama_makanan = getIntent().getStringExtra("namaMakanan");
        userId = getIntent().getStringExtra("userID");
        image = getIntent().getStringExtra("image");
        usernamePublisher = getIntent().getStringExtra("usernamePublisher");
        currentDate = getIntent().getStringExtra("currentDate");
        currentTime = getIntent().getStringExtra("currentTime");

        setIntent(id_makanan, nama_makanan, userId, image, usernamePublisher, currentDate, currentTime);
    }

    private void setIntent(String id_makanan, String nama_makanan, String userId, String image, String usernamePublisher, String currentDate, String currentTime) {
        tv_nama_makanan.setText(nama_makanan);
        Glide.with(getApplicationContext()).load(image).into(iv_gambar_makanan);
    }
}
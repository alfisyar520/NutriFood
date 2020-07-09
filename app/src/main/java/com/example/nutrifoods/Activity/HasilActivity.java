package com.example.nutrifoods.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nutrifoods.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class HasilActivity extends AppCompatActivity {

    private TextView tv_nama_makanan, tv_air, tv_energi, tv_protein, tv_lemak, tv_karbohidrat, tv_serat,
            tv_abu, tv_kalsium, tv_fosfor, tv_besi, tv_natrium, tv_kalium, tv_tembaga, tv_seng, tv_vitA, tv_viB1, tv_vitB2, tv_vitC;
    private ImageView iv_gambar_makanan;
    private String id_makanan, nama_makanan, userId, image, usernamePublisher,currentDate, currentTime, topMakanan;

    //firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        tv_nama_makanan = findViewById(R.id.tv_hasil_namaMakanan);
        iv_gambar_makanan = findViewById(R.id.iv_hasil_GambarMakanan);
        tv_air = findViewById(R.id.hasil_air);

        getIncomingIntent();

        readCSVFile();
    }

    private void getIncomingIntent() {
        id_makanan = getIntent().getStringExtra("id_makanan");
        nama_makanan = getIntent().getStringExtra("namaMakanan");
        userId = getIntent().getStringExtra("userID");
        image = getIntent().getStringExtra("image");
        usernamePublisher = getIntent().getStringExtra("usernamePublisher");
        currentDate = getIntent().getStringExtra("currentDate");
        currentTime = getIntent().getStringExtra("currentTime");
        topMakanan = getIntent().getStringExtra("topMakanan");


        setIntent(id_makanan, nama_makanan, userId, image, usernamePublisher, currentDate, currentTime, topMakanan);
    }

    private void setIntent(String id_makanan, String nama_makanan, String userId, String image, String usernamePublisher, String currentDate, String currentTime, String topMakanan) {
        tv_nama_makanan.setText(nama_makanan);
        Glide.with(getApplicationContext()).load(image).into(iv_gambar_makanan);
    }

    private void readCSVFile() {
        InputStream is = getResources().openRawResource(R.raw.data_nutrisi);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line;
        try {

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                Log.d("cona token", tokens[0]);
                if (tokens[0].equals(topMakanan)){
                    tv_air.setText("air "+tokens[1]);
                    //Log.d("coba hasil", "readCSVFile: masokk");
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
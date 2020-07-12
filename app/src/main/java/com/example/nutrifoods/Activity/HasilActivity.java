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
            tv_abu, tv_kalsium, tv_fosfor, tv_besi, tv_natrium, tv_kalium, tv_tembaga, tv_seng, tv_vitA, tv_vitB1, tv_vitB2, tv_vitC;
    private ImageView iv_gambar_makanan;
    private String id_makanan, nama_makanan, userId, image, usernamePublisher,currentDate, currentTime, topMakanan;

    //firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        tv_nama_makanan = findViewById(R.id.tv_hasil_namaMakanan);
        iv_gambar_makanan = findViewById(R.id.iv_hasil_GambarMakanan);

        //gizi
        tv_air = findViewById(R.id.hasil_air);
        tv_energi = findViewById(R.id.hasil_energi);
        tv_protein = findViewById(R.id.hasil_protein);
        tv_lemak = findViewById(R.id.hasil_lemak);
        tv_karbohidrat = findViewById(R.id.hasil_karbohidrat);
        tv_serat = findViewById(R.id.hasil_serat);
        tv_abu = findViewById(R.id.hasil_abu);
        tv_kalsium = findViewById(R.id.hasil_kalsium);
        tv_fosfor = findViewById(R.id.hasil_fosfor);
        tv_besi = findViewById(R.id.hasil_besi);
        tv_natrium = findViewById(R.id.hasil_natrium);
        tv_kalium = findViewById(R.id.hasil_kalium);
        tv_tembaga = findViewById(R.id.hasil_tembaga);
        tv_seng = findViewById(R.id.hasil_seng);
        tv_vitA = findViewById(R.id.hasil_vitA);
        tv_vitB1 = findViewById(R.id.hasil_vitB1);
        tv_vitB2 = findViewById(R.id.hasil_vitB2);
        tv_vitC = findViewById(R.id.hasil_vitC);

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
                    tv_air.setText("air "+ tokens[1]);
                    //Log.d("coba hasil", "readCSVFile: masokk");
                    tv_energi.setText("Energi "+ tokens[2]);
                    tv_protein.setText("Protein "+ tokens[3]);
                    tv_lemak.setText("Lemak "+ tokens[4]);
                    tv_karbohidrat.setText("Karbohidrat "+ tokens[5]);
                    tv_serat.setText("Serat "+ tokens[6]);
                    tv_abu.setText("Abu "+ tokens[7]);
                    tv_kalsium.setText("Kalsium "+ tokens[8]);
                    tv_fosfor.setText("Fosfor "+ tokens[9]);
                    tv_besi.setText("Besi "+ tokens[10]);
                    tv_natrium.setText("Natrium "+ tokens[11]);
                    tv_kalium.setText("Kalium "+ tokens[12]);
                    tv_tembaga.setText("Tembaga "+ tokens[13]);
                    tv_seng.setText("Seng "+ tokens[14]);
                    tv_vitA.setText("Vitamin A "+ tokens[15]);
                    tv_vitB1.setText("Vitamin B1 "+ tokens[16]);
                    tv_vitB2.setText("Vitamin B2 "+ tokens[17]);
                    tv_vitC.setText("Vitamin C "+ tokens[18]);
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
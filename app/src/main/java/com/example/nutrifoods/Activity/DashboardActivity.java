package com.example.nutrifoods.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nutrifoods.Adapter.ListPostHomeAdapter;
import com.example.nutrifoods.Model.MakananModel;
import com.example.nutrifoods.Model.User;
import com.example.nutrifoods.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton fab_add;
    private TextView usernameProfileHome;
    private ImageView imageProfileHome;
    private RecyclerView recyclerView;

    private String mUser;
    private MakananModel makananModel;

    Map<String, MakananModel> dataku;
    ArrayList<MakananModel> downModelArrayList = new ArrayList<>();


    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    //adapter
    private ListPostHomeAdapter myAdapter;


    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        makananModel = new MakananModel();
        dataku = new Hashtable<>();

        Toolbar toolbar = findViewById(R.id.topBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        fab_add = findViewById(R.id.dasboard_Post);
        usernameProfileHome = findViewById(R.id.nameprofile_home);
        imageProfileHome = findViewById(R.id.imageprofile_home);
        refreshLayout = findViewById(R.id.swapRefresh);
        recyclerView = findViewById(R.id.rv_home_makanan);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDataFromFirestore();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        myAdapter.notifyDataSetChanged();
                        getDataFromFirestore();
                    }
                }, 1000);
            }
        });



        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postAdd_start = new Intent(DashboardActivity.this, PostActivity.class);
                startActivity(postAdd_start);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usernameProfileHome.setText(user.getUsername());
                Glide.with(DashboardActivity.this).load(user.getImageUrl()).into(imageProfileHome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                return true;
        }
        return false;
    }

    private void getDataFromFirestore(){
        db.collection("Data Postingan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                dataku.clear();
                downModelArrayList.clear();

                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    makananModel = documentSnapshot.toObject(MakananModel.class);
                    downModelArrayList.add(makananModel);
                    dataku.put(documentSnapshot.getId(), makananModel);
                }
                myAdapter = new ListPostHomeAdapter(DashboardActivity.this, downModelArrayList, dataku);
                recyclerView.setAdapter(myAdapter);
                //Toast.makeText(DashboardActivity.this, "panggi", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            // here you can call your method !
        }
    }
}

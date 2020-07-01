package com.example.nutrifoods.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nutrifoods.Activity.HasilActivity;
import com.example.nutrifoods.Model.MakananModel;
import com.example.nutrifoods.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPostHomeAdapter extends RecyclerView.Adapter<ListPostHomeAdapter.ListViewHolder>{


    //private Map<String, String> percobaan = new Hashtable<>();

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String mUser;
    private Context mContext;
    private Map<String, MakananModel> data;
    private ArrayList<MakananModel> listMakanan;

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_makanan, parent, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        return new ListViewHolder(view);
    }

    public ListPostHomeAdapter(Context mContext, ArrayList<MakananModel> listMakanan, Map<String, MakananModel> data){
        this.mContext = mContext;
        this.listMakanan = listMakanan;
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ListPostHomeAdapter.ListViewHolder holder, int position) {

        /*
        int count = 0;
        db = FirebaseFirestore.getInstance();
        for (Map.Entry<String, MakananModel> e : data.entrySet()){
            percobaan.put(e.getKey(), e.getValue().getImage());
        }

         */



        holder.nama_makanan.setText(listMakanan.get(position).getNamaMakanan());
        Glide.with(mContext)
                .load(listMakanan.get(position).getImage())
                .into(holder.img_makanan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hasil_start = new Intent(mContext, HasilActivity.class);
                mContext.startActivity(hasil_start);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listMakanan.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_makanan;
        TextView nama_makanan;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            img_makanan = itemView.findViewById(R.id.profile_makanan);
            nama_makanan = itemView.findViewById(R.id.daftar_namaMakanan);
        }
    }
}


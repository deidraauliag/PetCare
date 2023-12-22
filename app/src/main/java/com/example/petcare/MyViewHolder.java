package com.example.petcare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvnama, tvtahun, tvbidang, tvharga, tvurl;
    ImageView img;
    View view;
    public MyViewHolder(@NonNull View itemView){
        super(itemView);
        tvnama = itemView.findViewById(R.id.nama);
        tvtahun = itemView.findViewById(R.id.tahun);
        tvbidang = itemView.findViewById(R.id.bidang);
        tvharga = itemView.findViewById(R.id.hargadoc);
        tvurl = itemView.findViewById(R.id.url);
        img = itemView.findViewById(R.id.imgFoto);
        view = itemView;
    }
}

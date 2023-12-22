package com.example.petcare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class vhQueue extends RecyclerView.ViewHolder {
    TextView tvnamadokter, tvanimname, tvanimtype;
    View view;

    public vhQueue(@NonNull View itemView) {
        super(itemView);
        tvnamadokter = itemView.findViewById(R.id.namadokter);
        tvanimname = itemView.findViewById(R.id.namahewan);
        tvanimtype = itemView.findViewById(R.id.tipehewan);
        view = itemView;
    }

    public void bindData(QueueModel model) {
        tvnamadokter.setText(model.getNamaDokter());
        tvanimname.setText(model.getAnimalName());
        tvanimtype.setText(model.getAnimalType());
    }
}
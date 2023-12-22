package com.example.petcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petcare.R;
import com.example.petcare.model.user;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class userAdapter extends FirebaseRecyclerAdapter<user, userAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public userAdapter(@NonNull FirebaseRecyclerOptions<user> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull user model) {
        holder.username.setText(model.getUname());
        holder.emailP.setText(model.getEmail());
        holder.NoHp.setText(model.getNohp());
        Glide.with(holder.avatarPic.getContext())
                .load(model.getAvatar())
                .placeholder(R.drawable.prof)
                .error(R.drawable.prof)
                .into(holder.avatarPic);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dataprofil, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView avatarPic;
        TextView username, emailP, NoHp;

        public myViewHolder(@NonNull View v){
            super(v);

            avatarPic = (ImageView) v.findViewById(R.id.avatar);
            username = (TextView) v.findViewById(R.id.uname);
            emailP = (TextView) v.findViewById(R.id.email);
            NoHp = (TextView) v.findViewById(R.id.nohp);
        }
    }
}

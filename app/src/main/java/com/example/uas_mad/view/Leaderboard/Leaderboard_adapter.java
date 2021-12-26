package com.example.uas_mad.view.Leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uas_mad.R;
import com.example.uas_mad.model.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard_adapter extends RecyclerView.Adapter<Leaderboard_adapter.ViewHolder> {

    private List<Leaderboard.Data> listleaderboard;

    public Leaderboard_adapter(List<Leaderboard.Data> listleaderboard) {
        this.listleaderboard = listleaderboard;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_number_leaderboard.setText(String.valueOf(position+1));
        holder.text_name_leaderboard.setText(listleaderboard.get(position).getName());
        holder.text_damage_leaderboard.setText(String.valueOf(listleaderboard.get(position).getTotal_damage()));
    }

    @Override
    public int getItemCount() {
        if (listleaderboard.size()<10){
            return listleaderboard.size();
        }else{
            return 10;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text_number_leaderboard, text_name_leaderboard, text_damage_leaderboard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_number_leaderboard = itemView.findViewById(R.id.text_number_leaderboard);
            text_name_leaderboard = itemView.findViewById(R.id.text_name_leaderboard);
            text_damage_leaderboard = itemView.findViewById(R.id.text_damage_leaderboard);
        }
    }
}

package com.example.workout_tracker_java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    private ArrayList id, title, date, reps, weight;

    int position;

    public CustomAdapter(Activity activity, Context context,
                         ArrayList id,
                         ArrayList title,
                         ArrayList date,
                         ArrayList reps,
                         ArrayList weight) {
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.title = title;
        this.date = date;
        this.reps = reps;
        this.weight = weight;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if (position < id.size() && position < title.size() && position < date.size()
                && position < reps.size() && position < weight.size()) {
            holder.id_txt.setText(String.valueOf(id.get(position)));
            holder.title_txt.setText(String.valueOf(date.get(position)));
            holder.date_txt.setText(String.valueOf(title.get(position)));
            holder.reps_txt.setText(String.valueOf(reps.get(position)));
            holder.weight_txt.setText(String.valueOf(weight.get(position)));
            holder.mainLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("id", String.valueOf(id.get(position)));
                    intent.putExtra("title", String.valueOf(title.get(position)));
                    intent.putExtra("date", String.valueOf(date.get(position)));
                    intent.putExtra("reps", String.valueOf(reps.get(position)));
                    intent.putExtra("weight", String.valueOf(weight.get(position)));
                    activity.startActivityForResult(intent, 1);
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call the deleteData method and pass the ID of the row to delete
                    deleteData(String.valueOf(id.get(position)));
                }
            });
        }
    }

    private void deleteData(String id) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(context);
        myDB.deleteRow(id);
        notifyItemRemoved(position);
        Toast.makeText(context, "Row deleted", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_txt, title_txt, date_txt, reps_txt, weight_txt;
        ConstraintLayout mainLayout;
        Button deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.id);
            title_txt = itemView.findViewById(R.id.title_txt);
            date_txt = itemView.findViewById(R.id.date);
            reps_txt = itemView.findViewById(R.id.reps);
            weight_txt = itemView.findViewById(R.id.weight);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}

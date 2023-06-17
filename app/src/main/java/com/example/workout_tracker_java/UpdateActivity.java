package com.example.workout_tracker_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText title_input, date_input, reps_input, weight_input;
    Button update_button;

    String id;
    String title;
    String date;
    int reps;
    int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        date_input = findViewById(R.id.date_input2);
        reps_input = findViewById(R.id.reps_input2);
        weight_input = findViewById(R.id.weight_input2);
        update_button = findViewById(R.id.update_button);
        getAndSetIntentData();
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedTitle = title_input.getText().toString().trim();
                String updatedDate = date_input.getText().toString().trim();
                int updatedReps = Integer.parseInt(reps_input.getText().toString().trim());
                int updatedWeight = Integer.parseInt(weight_input.getText().toString().trim());

                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.updateData(id, updatedTitle, updatedDate, updatedReps, updatedWeight);
            }
        });

    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("date")
                && getIntent().hasExtra("reps") && getIntent().hasExtra("weight")) {
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            date = getIntent().getStringExtra("date");
            reps = Integer.parseInt(getIntent().getStringExtra("reps"));
            weight = Integer.parseInt(getIntent().getStringExtra("weight"));

            title_input.setText(title);
            date_input.setText(date);
            reps_input.setText(String.valueOf(reps)); // Convert reps to string
            weight_input.setText(String.valueOf(weight)); // Convert weight to string
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

}
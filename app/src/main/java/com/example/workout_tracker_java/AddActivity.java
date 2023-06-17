package com.example.workout_tracker_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText title_input, reps_input, date_input, weight_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        date_input = findViewById(R.id.date_input);
        reps_input = findViewById(R.id.reps_input);
        weight_input = findViewById(R.id.weight_input);

        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_input.getText().toString().trim();
                String date = date_input.getText().toString().trim();
                int reps = Integer.parseInt(reps_input.getText().toString().trim());
                int weight = Integer.parseInt(weight_input.getText().toString().trim());

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addWorkout(title, date, reps, weight);
            }
        });
    }
}

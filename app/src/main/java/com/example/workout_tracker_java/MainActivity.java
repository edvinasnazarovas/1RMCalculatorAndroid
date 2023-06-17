package com.example.workout_tracker_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    // Inicilizacja pomocnika klasy bazy danej
    MyDatabaseHelper myDB;
    ArrayList<String> id, title, date, reps, weight;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        title = new ArrayList<>();
        date = new ArrayList<>();
        reps = new ArrayList<>();
        weight = new ArrayList<>();

        customAdapter = new CustomAdapter(MainActivity.this, this, id, title, date, reps, weight); // Initialize customAdapter
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        storeDataInArrays();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        id.clear();
        title.clear();
        date.clear();
        reps.clear();
        weight.clear();

        new MyAsyncTask(this).execute();
    }

    private static class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<Double>> {
        private WeakReference<MainActivity> activityReference;

        MyAsyncTask(MainActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected ArrayList<Double> doInBackground(Void... voids) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }

            Cursor cursor = activity.myDB.readAllData();
            if (cursor.getCount() == 0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "No data.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                ArrayList<Double> oneRepMaxList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    activity.id.add(cursor.getString(0));
                    activity.title.add(cursor.getString(1));
                    activity.date.add(cursor.getString(2));
                    activity.reps.add(cursor.getString(3));
                    activity.weight.add(cursor.getString(4));

                    int rep = Integer.parseInt(cursor.getString(3));
                    int weight = Integer.parseInt(cursor.getString(4));
                    double oneRepMax = weight * rep * 0.0333 + weight;
                    oneRepMaxList.add(oneRepMax);
                }

                return oneRepMaxList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Double> oneRepMaxList) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            if (activity.customAdapter != null) {
                activity.customAdapter.notifyDataSetChanged();
            }

            if (oneRepMaxList != null) {
                for (int i = 0; i < oneRepMaxList.size(); i++) {
                    double oneRepMax = oneRepMaxList.get(i);
                    TextView displayMaxTextView = activity.findViewById(R.id.displayMax);
                    String formattedOneRepMax = String.format("%.0f kg", oneRepMax);
                    displayMaxTextView.setText(formattedOneRepMax);
                }
            }
        }
    }
}
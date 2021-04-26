package com.hamzahch.alphapic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views
        btnStart = findViewById(R.id.btnStart);
        btnHistory = findViewById(R.id.btnHistory);
    }

    public void onClickStart(View view) {
        btnStart.playSoundEffect(0);
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void onClickHistory(View view) {
        btnHistory.playSoundEffect(1);
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }

}
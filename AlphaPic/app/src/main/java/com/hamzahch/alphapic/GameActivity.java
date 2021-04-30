package com.hamzahch.alphapic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.util.Random;

public class GameActivity extends Activity {

    private int[] imgResources;
    private int[] soundResources;
    private MediaPlayer mPlayer;
    private Chronometer mTimer;
    private ImageButton mCurrBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mTimer = findViewById(R.id.stopwatch);

        // define boxes and sounds
        imgResources = new int[] { R.id.boxA, R.id.boxB, R.id.boxC, R.id.boxD, R.id.boxE, R.id.boxF, R.id.boxG,
            R.id.boxH, R.id.boxI, R.id.boxJ, R.id.boxK, R.id.boxL, R.id.boxM, R.id.boxN, R.id.boxO,
            R.id.boxP, R.id.boxQ, R.id.boxR, R.id.boxS, R.id.boxT, R.id.boxU, R.id.boxV, R.id.boxW,
            R.id.boxX, R.id.boxY, R.id.boxZ };
        soundResources = new int[] { R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g,
            R.raw.h, R.raw.i, R.raw.j, R.raw.k, R.raw.l, R.raw.m, R.raw.n, R.raw.o, R.raw.p, R.raw.q,
            R.raw.r, R.raw.s, R.raw.t, R.raw.u, R.raw.v, R.raw.w, R.raw.x, R.raw.y, R.raw.z };

        // make boxes unclickable
        for (int res : imgResources) {
            ImageButton btn = findViewById(res);
            btn.setClickable(false);
        }

        // start background music
        Intent i = new Intent(this, MusicService.class);
        startService(i);

        // start new round
        startRound();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            mCurrBox.setImageBitmap(img);
            stopRound();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // stop background music
        Intent i = new Intent(this, MusicService.class);
        stopService(i);
        mPlayer.release();
    }

    public void onBoxClick(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
    }

    public void onExit(View view) {
        finish();
    }

    public void startRound() {
        // start timer
        mTimer.start();

        // select random box
        Random r = new Random();
        int box = r.nextInt(imgResources.length);
        mCurrBox = findViewById(imgResources[box]);
        mCurrBox.setBackgroundResource(R.drawable.image_border_active);
        mCurrBox.setClickable(true);
        mPlayer = MediaPlayer.create(this, soundResources[box]);
        mPlayer.start();
    }

    public void stopRound() {
        // reset
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.stop();
        mCurrBox.setClickable(false);
        mCurrBox.setBackgroundResource(R.drawable.image_border);
        startRound();
    }

}
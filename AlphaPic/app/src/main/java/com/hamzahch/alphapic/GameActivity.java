package com.hamzahch.alphapic;

import android.app.Activity;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageButton;

import java.util.Random;

public class GameActivity extends Activity {

    private int[] imgResources;
    private int[] soundResources;
    private SoundPool mSoundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // define boxes and sounds
        imgResources = new int[] { R.id.boxA, R.id.boxB, R.id.boxC, R.id.boxD, R.id.boxE, R.id.boxF, R.id.boxG,
            R.id.boxH, R.id.boxI, R.id.boxJ, R.id.boxK, R.id.boxL, R.id.boxM, R.id.boxN, R.id.boxO,
            R.id.boxP, R.id.boxQ, R.id.boxR, R.id.boxS, R.id.boxT, R.id.boxU, R.id.boxV, R.id.boxW,
            R.id.boxX, R.id.boxY, R.id.boxZ };
        soundResources = new int[] { R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g,
            R.raw.h, R.raw.i, R.raw.j, R.raw.k, R.raw.l, R.raw.m, R.raw.n, R.raw.o, R.raw.p, R.raw.q,
            R.raw.r, R.raw.s, R.raw.t, R.raw.u, R.raw.v, R.raw.w, R.raw.x, R.raw.y, R.raw.z };

        // start background music
        Intent i = new Intent(this, MusicService.class);
        startService(i);

        // select random box
        Random r = new Random();
        int box = r.nextInt(imgResources.length);
        ImageButton img = findViewById(imgResources[box]);
        img.setBackgroundResource(R.drawable.image_border_active);
        mSoundPool = new SoundPool.Builder().build();
        int sound = mSoundPool.load(this, soundResources[box], 1);
        mSoundPool.play(sound, 1, 1, 0, 0, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // stop background music
        Intent i = new Intent(this, MusicService.class);
        stopService(i);
        mSoundPool.release();
    }
}
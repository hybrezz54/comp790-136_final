package com.hamzahch.alphapic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

public class GameActivity extends Activity {

    private List<Integer> imgResources;
    private List<Integer> soundResources;
    private MediaPlayer mPlayer;
    private Chronometer mTimer;
    private ImageButton mCurrBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mTimer = findViewById(R.id.stopwatch);

        // define boxes and sounds
        imgResources = new ArrayList(Arrays.asList(R.id.boxA, R.id.boxB, R.id.boxC, R.id.boxD, R.id.boxE,
                R.id.boxF, R.id.boxG, R.id.boxH, R.id.boxI, R.id.boxJ, R.id.boxK, R.id.boxL, R.id.boxM,
                R.id.boxN, R.id.boxO, R.id.boxP, R.id.boxQ, R.id.boxR, R.id.boxS, R.id.boxT, R.id.boxU,
                R.id.boxV, R.id.boxW, R.id.boxX, R.id.boxY, R.id.boxZ));
        soundResources = new ArrayList(Arrays.asList(R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f,
                R.raw.g, R.raw.h, R.raw.i, R.raw.j, R.raw.k, R.raw.l, R.raw.m, R.raw.n, R.raw.o, R.raw.p,
                R.raw.q, R.raw.r, R.raw.s, R.raw.t, R.raw.u, R.raw.v, R.raw.w, R.raw.x, R.raw.y, R.raw.z));

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

            try {
                // create classifier instance
                ImageClassifierOptions options = ImageClassifierOptions.builder()
                        .setMaxResults(3).build();
                ImageClassifier classifier = ImageClassifier.createFromFileAndOptions(this,
                        "mobilenet_v1_1.0_224_quant.tflite", options);

                // set props
                TensorImage image = TensorImage.fromBitmap(img);
                int width = img.getWidth();
                int height = img.getHeight();
                int cropSize = min(width, height);

                // run inference
                ImageProcessingOptions imageOptions =
                        ImageProcessingOptions.builder()
                                // .setOrientation(getOrientation(sensorOrientation))
                                // Set the ROI to the center of the image.
                                .setRoi(
                                        new Rect(
                                                /*left=*/ (width - cropSize) / 2,
                                                /*top=*/ (height - cropSize) / 2,
                                                /*right=*/ (width + cropSize) / 2,
                                                /*bottom=*/ (height + cropSize) / 2))
                                .build();

                List<Classifications> classifications = classifier.classify(image,
                        imageOptions);
                List<Integer> results = new ArrayList<>();

                classifications.forEach(classification -> {
                    classification.getCategories().forEach(category -> {
                        results.add(Integer.parseInt(category.getLabel()));
                    });
                });

                // set up results
                List<String> labels = new ArrayList<>();
                Collections.sort(results);
                BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("labels_mobilenet_quant_v1_224.txt")));
                int i = 0;

                // iterate over labels
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    if (results.size() <= 0)
                        break;

                    if (i == results.get(0)) {
                        labels.add(line);
                        results.remove(0);
                    }

                    i++;
                }

                br.close();
                Log.e("FINAL", TextUtils.join(";", labels));
            } catch (IOException e) {
                e.printStackTrace();
            }


//            mCurrBox.setBackgroundResource(R.drawable.image_border_correct);
//            stopRound();
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
        // check if boxes filled
        if (imgResources.size() < 1) {
            mPlayer = MediaPlayer.create(this, R.raw.end);
            mPlayer.start();
            return;
        }

        // start timer
        mTimer.start();

        // select random box
        Random r = new Random();
        int box = r.nextInt(imgResources.size());
        mCurrBox = findViewById(imgResources.get(box));
        imgResources.remove(box);
        mCurrBox.setBackgroundResource(R.drawable.image_border_active);
        mCurrBox.setClickable(true);

        // play appropriate sound
        mPlayer = MediaPlayer.create(this, soundResources.get(box));
        soundResources.remove(box);
        mPlayer.start();
    }

    public void stopRound() {
        // reset
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.stop();
        mCurrBox.setClickable(false);
        startRound();
    }

}
package com.hamzahch.alphapic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Arrays;

public class PhotoActivity extends Activity {

    private ListView mPhotoView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // setup
        String letter = getIntent().getStringExtra(getString(R.string.extra_letter_key));
        mPhotoView = findViewById(R.id.photoView);
        mTextView = findViewById(R.id.textLetter);
        mTextView.setText(letter);

        // add to adapter
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = Arrays.stream(dir.listFiles())
                .filter(f -> f.getName().startsWith(letter))
                .limit(3)
                .toArray(File[]::new);
        FileAdapter adapter = new FileAdapter(this, files);
        mPhotoView.setAdapter(adapter);

        // handle item click
        mPhotoView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    files[position]));
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(i);
        });
    }

    public void onExit(View view) {
        finish();
    }
}
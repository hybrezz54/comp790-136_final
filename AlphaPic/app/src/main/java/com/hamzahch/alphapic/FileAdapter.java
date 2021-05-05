package com.hamzahch.alphapic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.List;

public class FileAdapter extends ArrayAdapter<File> {

    public FileAdapter(@NonNull Context context, @NonNull File[] objects) {
        super(context, 0, objects);
    }

    public FileAdapter(@NonNull Context context, @NonNull List<File> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get file
        File file = getItem(position);

        // inflate view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_file,
                    parent, false);
        }

        // Lookup view for data population
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textTitle = convertView.findViewById(R.id.textTitle);
        TextView textSubtitle = convertView.findViewById(R.id.textSubtitle);

        // parse file name
        String[] parsed = file.getName().split("_");
        float elapsedSec = Integer.parseInt(parsed[1]) / 1000f;
        textTitle.setText(elapsedSec + " secs");
        String timeTaken = parsed[2].substring(0, 14);
        String year = timeTaken.substring(0, 4);
        String month = timeTaken.substring(4, 6);
        String day = timeTaken.substring(6, 8);
        String hr = timeTaken.substring(8, 10);
        String min = timeTaken.substring(10, 12);
        String sec = timeTaken.substring(12, 14);
        timeTaken = day + "/" + month + "/" + year + " " + hr + ":" +
                min + ":" + sec;
        textSubtitle.setText(timeTaken);

        // add thumbnail
        Bitmap img = BitmapFactory.decodeFile(file.getPath());
        if (img != null) imageView.setImageBitmap(img);

        // return file name
        return convertView;
    }
}

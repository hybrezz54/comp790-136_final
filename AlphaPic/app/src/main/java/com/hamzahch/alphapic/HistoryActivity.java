package com.hamzahch.alphapic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryActivity extends Activity {

    private BarChart mChart;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // find views
        mChart = findViewById(R.id.barChart);
        mListView = findViewById(R.id.listView);

        // init vars
        ArrayList<String> letters = new ArrayList(Arrays.asList("a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
                "v", "w", "x", "y", "z"));
        ArrayList<BarEntry> entries = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file_key),
                Context.MODE_PRIVATE);

        // update entries
        for (int i = 0; i < letters.size(); i++) {
            String letter = letters.get(i);
            float avg = (float) prefs.getInt(letter + "_avgtime", 0) / 1000;
            entries.add(new BarEntry(i, avg));
        }

        // update graph
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        mChart.setData(barData);
        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.animateY(1000);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(letters));
        xAxis.setLabelCount(26);
        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-25);
        xAxis.setDrawGridLines(true);
        YAxis yAxis = mChart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.invalidate();

        // set listview adapter
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                letters);
        mListView.setAdapter(adapter);
    }

    public void onExit(View view) {
        finish();
    }
}
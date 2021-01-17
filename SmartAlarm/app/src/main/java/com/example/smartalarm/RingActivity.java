package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RingActivity extends AppCompatActivity {

    public TextView ring_current_time,ring_current_date;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        ring_current_time = findViewById(R.id.current_time_text);
        ring_current_date = findViewById(R.id.current_date_text);

        ReturnCurrentDate();
        ReturnCurrentTime();
    }

    public void ReturnCurrentTime()
    {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("h:mm a");
        String date = dateFormat.format(calendar.getTime());
        ring_current_time.setText(date);
    }
    public void ReturnCurrentDate()
    {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE MMM d");
        String date = dateFormat.format(calendar.getTime());
        ring_current_date.setText(date);
    }
}
package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RingActivity extends AppCompatActivity {

    public TextView ring_current_time,ring_current_date;
    public Button open_camera_button;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    //  Booleans
    public boolean switched_to_cam_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

        ring_current_time = findViewById(R.id.current_time_text);
        ring_current_date = findViewById(R.id.current_date_text);
        open_camera_button = findViewById(R.id.gesture_button);

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

    //  Switched to Camera Activity
    public void SwitchToCameraActivity()
    {
        if(!switched_to_cam_act)
        {
            Intent intent = new Intent(RingActivity.this,CameraActivity.class);
            startActivity(intent);
            switched_to_cam_act = true;
        }
    }

    //  OnClickListener for camera button
    public void CameraButton(View view)
    {
        SwitchToCameraActivity();
    }
}
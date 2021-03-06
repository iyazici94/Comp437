package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //Initialize variables

    //  Time picker related
    TextView tvTimer1;
    int t1Hour,t1Minute;
    TimePickerDialog timePickerDialog;
    Button stop_alarm_button;
    Ringtone r;
    MyRingToneHelper ringToneHelper;
    MyBitmapKeeper bitmapKeeper;
    //  Booleans
    boolean isPoseSelected;
    protected boolean switched_to_ring_act;
    protected boolean switched_to_main_act;

    public MyPoseKeeper poseKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Ringtone
        r = RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        ringToneHelper = new MyRingToneHelper(r);

        CreateTimePickerDialog();
        AlarmTimer();

        if(MyPoseKeeper.instance == null)
        {
            poseKeeper = new MyPoseKeeper();
        }
        else
        {
            poseKeeper = MyPoseKeeper.instance;
        }

        if(MyBitmapKeeper.instance == null)
        {
            bitmapKeeper = new MyBitmapKeeper();
        }
        else
        {
            bitmapKeeper = MyBitmapKeeper.instance;
        }

        //  SET BOOLEANS



    }

    //  Creates the time picker dialog
    protected void CreateTimePickerDialog()
    {
        //Assigning variables
        tvTimer1 = findViewById(R.id.set_timer2);
        stop_alarm_button = findViewById((R.id.stopButton));
        tvTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(poseKeeper.isPoseSelected)
                {
                    //Initialize time picker dialog
                    timePickerDialog = new TimePickerDialog(
                            MainActivity.this,
                            2,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    //Initialize hour and minute
                                    t1Hour = hourOfDay;
                                    t1Minute= minute;
                                    //Initialize calendar
                                    Calendar calendar = Calendar.getInstance();
                                    //Set hour and minute
                                    calendar.set(0,0,0,t1Hour,t1Minute);
                                    //Set selected time on text view
                                    tvTimer1.setText(DateFormat.format("hh:mm aa",calendar));
                                }
                            },12,0,false
                    );
                    //Displayed previous selected time
                    timePickerDialog.updateTime(t1Hour,t1Minute);
                    //Show dialog
                    timePickerDialog.show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "SELECT A POSE FIRST", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //  Checks whether time has come for alarm
    public void AlarmTimer()
    {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(ReturnCurrentTime().equals(AlarmTime()) ||ringToneHelper.canPlay)
                {
                    if(!ringToneHelper.isDeactivated)
                    {
                        ringToneHelper.canPlay = true;
                    }
                    ringToneHelper.MyRingtonePlay();
                    SwitchToRingActivity();
                }
                else
                {
                    ringToneHelper.MyRingtoneStop();
                }
            }
        },0,1000);  //  Will check it every second
    }


    //  Returns the alarm time
    public String AlarmTime()
    {
        String alarm_time_string = tvTimer1.getText().toString();
        return  alarm_time_string;
    }

    public String ReturnCurrentTime()
    {
        String current_time = DateFormat.format("hh:mm aa",Calendar.getInstance().getTime()).toString();
        return  current_time;
    }

    //  OnClickListener for out button
    public void ListButton(View view)
    {
        /*
        r.stop();
        isStopped = true;
         */
        Intent intent = new Intent(MainActivity.this,ListActivity.class);
        startActivity(intent);
    }

    public void SwitchToRingActivity()
    {
        if(!switched_to_ring_act)
        {
            Intent intent = new Intent(MainActivity.this,RingActivity.class);
            startActivity(intent);
            switched_to_ring_act = true;
        }
    }

    public void SwitchToListActivity()
    {
        Intent intent = new Intent(MainActivity.this,ListActivity.class);
        startActivity(intent);
    }

}
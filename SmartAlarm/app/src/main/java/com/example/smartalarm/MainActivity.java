package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Ringtone
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        CreateTimePickerDialog();
        AlarmTimer(r);

    }

    //  Creates the time picker dialog
    protected void CreateTimePickerDialog()
    {
        //Assigning variables
        tvTimer1 = findViewById(R.id.set_timer2);

        tvTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker dialog
                timePickerDialog = new TimePickerDialog(
                        MainActivity.this,
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
        });
    }



    //  Checks whether time has come for alarm
    public void AlarmTimer(Ringtone r)
    {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(ReturnCurrentTime().equals(AlarmTime()))
                {
                    r.play();
                    Log.d("TIMERIF",AlarmTime() + " " + ReturnCurrentTime());
                }
                else
                {
                    r.stop();
                    Log.d("TIMERELSE",AlarmTime() + " " + ReturnCurrentTime());
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
}
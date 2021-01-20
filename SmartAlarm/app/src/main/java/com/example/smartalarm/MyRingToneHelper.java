package com.example.smartalarm;

import android.media.Ringtone;

public class MyRingToneHelper {

    public static MyRingToneHelper instance;
    public Ringtone r;

    public boolean canPlay;
    public boolean isDeactivated;

    public MyRingToneHelper(Ringtone r)
    {
        this.r = r;
        instance = this;
    }

    public void MyRingtonePlay()
    {
        if(canPlay)
            r.play();
        else
        {
            r.stop();
        }
    }

    public void MyRingtoneStop()
    {
        r.stop();
    }
}

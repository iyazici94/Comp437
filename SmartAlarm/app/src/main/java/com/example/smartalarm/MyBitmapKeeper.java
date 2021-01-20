package com.example.smartalarm;

import android.graphics.Bitmap;

public class MyBitmapKeeper {
    public static MyBitmapKeeper instance;
    public Bitmap bitmap;

    public MyBitmapKeeper()
    {
        instance = this;
    }
}

package com.example.smartalarm;

public class MyPoseKeeper {

    public static MyPoseKeeper instance;
    public boolean isPoseSelected;

    public boolean isSmile;
    public boolean isHandOnChin;
    public boolean isThumbsUp;
    public boolean isWristShoulder;

    public MyPoseKeeper()
    {
        instance = this;
        isPoseSelected = false;
    }
}

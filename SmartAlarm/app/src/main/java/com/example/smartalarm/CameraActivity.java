package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.camerakit.CameraKitView;

public class CameraActivity extends AppCompatActivity {

    private CameraKitView cameraView;
    private Button faceDetectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        faceDetectButton = findViewById(R.id.detect_gesture_btn);
        cameraView = findViewById(R.id.camera_view);
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraView.stop();
    }
}
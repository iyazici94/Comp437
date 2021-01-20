package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import static java.lang.Math.atan2;

public class PoseActivity extends AppCompatActivity {

    PoseDetector poseDetector;
    CameraKitView cameraKitView;
    Button handDetectButton;

    boolean isSucces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pose);
        FirebaseApp.initializeApp(this);

        cameraKitView = findViewById(R.id.my_camera_view);
        handDetectButton = findViewById(R.id.detect_pose_btn);


        handDetectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback(){
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        InputImage image;
                        Bitmap itmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Bitmap bitmap = Bitmap.createScaledBitmap(itmap, 480, 360, false);
                        MyBitmapKeeper.instance.bitmap = bitmap;
                        image = InputImage.fromBitmap(bitmap,0);

                        ProcessPoseDetection(image);
                    }
                });
            }
        });
    }

    public void ProcessPoseDetection(InputImage image)
    {
        AccuratePoseDetectorOptions options = new AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                .build();

        poseDetector = PoseDetection.getClient(options);

        Task<Pose> result =
                poseDetector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<Pose>() {
                                    @Override
                                    public void onSuccess(Pose pose) {
                                        // Task completed successfully

                                        //  TWO THUMBS
                                        PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
                                        PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
                                        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
                                        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
                                        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
                                        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
                                        PoseLandmark right_mouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);
                                        PoseLandmark left_mouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
                                        PoseLandmark right_wrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
                                        PoseLandmark left_wrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
                                        PoseLandmark left_index = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
                                        PoseLandmark right_index = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);

                                        double thumbAngle;

                                        //  THUMBS UP
                                        if(MyPoseKeeper.instance.isThumbsUp)
                                        {
                                            //  RIGHT THUMB
                                            if(rightShoulder != null||rightElbow != null||rightThumb != null)
                                            {

                                                float distance = rightThumb.getPosition().y - right_mouth.getPosition().y;

                                                if(distance >= -60 && distance <= 45)
                                                    isSucces = true;
                                            }

                                            //  LEFT THUMB
                                            else if(leftShoulder != null||leftElbow != null||leftThumb != null)
                                            {

                                                float distance = leftThumb.getPosition().y - left_mouth.getPosition().y;
                                                if(distance >= -60 && distance <= 45)
                                                    isSucces = true;
                                            }


                                            if(isSucces)
                                            {
                                                Toast.makeText(PoseActivity.this, "GOOD MORNING!!", Toast.LENGTH_SHORT).show();
                                                SwitchToMorningActivity();
                                            }
                                        }


                                        if(MyPoseKeeper.instance.isHandOnChin)
                                        {
                                            //  HAND ON THE FACE

                                            //  LEFT
                                            if(leftThumb != null||left_mouth != null||left_index != null)
                                            {

                                                thumbAngle = getAngle(
                                                        left_index,
                                                        left_mouth,
                                                        leftThumb);

                                                if(thumbAngle>= 0.5 && thumbAngle <= 2)
                                                    isSucces = true;
                                            }

                                            //  RIGHT
                                            if(rightThumb != null||right_mouth != null||right_index != null)
                                            {
                                                thumbAngle = getAngle(
                                                        right_index,
                                                        right_mouth,
                                                        rightThumb);
                                                Log.i("MYRIGHTTHUMBANGLE",Double.toString(thumbAngle));
                                                if(thumbAngle >= 2 && thumbAngle <= 5)
                                                    isSucces = true;
                                            }

                                            if(isSucces)
                                            {
                                                Toast.makeText(PoseActivity.this, "GOOD MORNING!!", Toast.LENGTH_SHORT).show();
                                                SwitchToMorningActivity();
                                            }
                                        }

                                        if(MyPoseKeeper.instance.isWristShoulder)
                                        {
                                            double angle;
                                            if(leftShoulder != null && right_wrist != null && rightElbow != null)
                                            {
                                                 angle = getAngle(
                                                         leftShoulder,
                                                         right_wrist,
                                                         rightElbow);
                                                 if(angle >= 100)
                                                     isSucces = true;
                                            }

                                            if(isSucces)
                                            {
                                                Toast.makeText(PoseActivity.this, "GOOD MORNING!!", Toast.LENGTH_SHORT).show();
                                                SwitchToMorningActivity();
                                            }
                                        }


                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }

    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }

    public void SwitchToMorningActivity()
    {
        Intent intent = new Intent(PoseActivity.this,MorningActivity.class);
        MyRingToneHelper.instance.canPlay = false;
        MyRingToneHelper.instance.isDeactivated = true;
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraKitView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraKitView.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
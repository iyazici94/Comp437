package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private Button faceDetectButton;
    FaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraKitView = findViewById(R.id.camera_view);
        faceDetectButton = findViewById(R.id.detect_gesture_btn);

        FirebaseApp.initializeApp(this);

        faceDetectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cameraKitView.captureImage(new CameraKitView.ImageCallback(){
                    @Override
                    public void onImage(CameraKitView cameraKitView,final byte[] bytes) {
                        InputImage image;

                        Bitmap itmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Bitmap bitmap = Bitmap.createScaledBitmap(itmap, 480, 360, false);
                        image = InputImage.fromBitmap(bitmap,270);

                        MyBitmapKeeper.instance.bitmap = bitmap;
                        ProcessFaceDetection(image);
                    }
                });
            }
        });

    }



    public void ProcessFaceDetection(InputImage image)
    {
        // [START set_detector_options]
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();
        // [END set_detector_options]

        // [START get_detector]
        detector = FaceDetection.getClient(options);

        // [START run_detector]
        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        Log.i("SUCCESSSMILE","succesfully detected");
                                        // Task completed successfully
                                        // [START_EXCLUDE]
                                        // [START get_face_info]
                                        //Log.i("MySmileProb",faces.get(0).getSmilingProbability().toString());
                                        for (Face face : faces) {
                                            Rect bounds = face.getBoundingBox();
                                            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):


                                            // If classification was enabled:
                                            if (face.getSmilingProbability() != null) {
                                                float smileProb = face.getSmilingProbability();
                                                String tmp = Float.toString(smileProb);
                                                Log.i("MYSMILEPROB",tmp);
                                                //  THIS CODE MIGHT CAUSES ERRORS, TEST THIS!!!!
                                                if(smileProb >= 0.3)
                                                {
                                                    Toast.makeText(CameraActivity.this, "GOOD MORNING!!", Toast.LENGTH_SHORT).show();
                                                    SwitchToMorningActivity();
                                                }
                                                else
                                                {
                                                    Toast.makeText(CameraActivity.this, "SMILE WIDER :)", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(CameraActivity.this, "NO FACE DETECTED!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        // [END get_face_info]
                                        // [END_EXCLUDE]
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


    public void SwitchToMorningActivity()
    {
        Intent intent = new Intent(CameraActivity.this,MorningActivity.class);
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
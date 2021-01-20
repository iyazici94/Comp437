package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listview);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("SMILE");
        arrayList.add("THUMBSUP");
        arrayList.add("HANDONCHIN");
        arrayList.add("WRISTHOULDER");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyPoseKeeper.instance.isPoseSelected = true;
                //  Depending on the item chosen, sends intent for booleans
                switch (position)
                {
                    case 0:
                        MyPoseKeeper.instance.isSmile = true;
                        MyPoseKeeper.instance.isThumbsUp = false;
                        MyPoseKeeper.instance.isHandOnChin = false;
                        MyPoseKeeper.instance.isWristShoulder = false;
                        SwitchToMainActivity("SMILE");
                        break;
                    case 1:
                        MyPoseKeeper.instance.isSmile = false;
                        MyPoseKeeper.instance.isThumbsUp = true;
                        MyPoseKeeper.instance.isHandOnChin = false;
                        MyPoseKeeper.instance.isWristShoulder = false;
                        SwitchToMainActivity("THUMBSUP");
                        break;
                    case 2:
                        MyPoseKeeper.instance.isSmile = false;
                        MyPoseKeeper.instance.isThumbsUp = false;
                        MyPoseKeeper.instance.isHandOnChin = true;
                        MyPoseKeeper.instance.isWristShoulder = false;
                        SwitchToMainActivity("HANDONCHIN");
                        break;
                    case 3:
                        MyPoseKeeper.instance.isSmile = false;
                        MyPoseKeeper.instance.isThumbsUp = false;
                        MyPoseKeeper.instance.isHandOnChin = false;
                        MyPoseKeeper.instance.isWristShoulder = true;
                        SwitchToMainActivity("WRISTSHOULDER");
                        break;
                    default:
                        // empty
                }
            }
        });
    }

    public void SwitchToMainActivity(String name)
    {
        Intent intent = new Intent(ListActivity.this,MainActivity.class);
        Toast.makeText(ListActivity.this, name + " pose  is chosen", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
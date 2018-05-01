package com.example.niklascorrea.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.squareup.picasso.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MP7:Main";
    private static RequestQueue requestQueue;

    Button searchInfo;
    AutoCompleteTextView buildingSuggestions;
    EditText roomNumber;
    ImageView map;
    TextView buildingsTag;
    TextView roomTag;

    private static String BUILDING_NUMBER = "";
    private static String FLOOR_NUMBER = "";
    private static String MAP_URL = "http://ada.fs.illinois.edu/" + BUILDING_NUMBER + "PLAN" + FLOOR_NUMBER + ".gif";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomNumber = findViewById(R.id.room);
        map = findViewById(R.id.imageView);
        buildingsTag = findViewById(R.id.buildingTag);
        roomTag = findViewById(R.id.roomTag);
        searchInfo = findViewById(R.id.searchInfo);
        searchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInfo.setVisibility(View.INVISIBLE);
                buildingSuggestions.setVisibility(View.INVISIBLE);
                roomNumber.setVisibility(View.INVISIBLE);
                buildingsTag.setVisibility(View.INVISIBLE);
                roomTag.setVisibility(View.INVISIBLE);
                formatBuildingAndFloorNumbers();
                search();
            }
        });

        buildingSuggestions = findViewById(R.id.building);
        buildingSuggestions.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, BuildingsList.BUILDINGS_LIST));
        buildingSuggestions.setThreshold(1);
        buildingSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TextView ayy = (TextView) view;
                    String buildingName = ayy.getText().toString();
                    Toast.makeText(MainActivity.this, buildingName, Toast.LENGTH_SHORT);
                    BUILDING_NUMBER = buildingName.substring(buildingName.indexOf("#") + 1, buildingName.indexOf(")"));
                } catch(Exception e) {
                    Log.d(TAG, "EXCEPTION: " + e);
                }
            }
        });
    }

    private void search() {
        MAP_URL = "http://ada.fs.illinois.edu/" + BUILDING_NUMBER + "Plan" + FLOOR_NUMBER + ".gif";
        Picasso.get().load(MAP_URL).into(map);
    }

    private void formatBuildingAndFloorNumbers() {
        final int buildingNumberLength = 4;
        int zerosToAdd = buildingNumberLength - BUILDING_NUMBER.length();
        String format = "";
        for (int i = 0; i < zerosToAdd; i++) {
            format += "0";
        }
        BUILDING_NUMBER = format + BUILDING_NUMBER;
        FLOOR_NUMBER = roomNumber.getText().toString().trim();
        if (FLOOR_NUMBER.charAt(0) == '0' || FLOOR_NUMBER.length() < 4) {
            FLOOR_NUMBER = "B";
        } else {
            FLOOR_NUMBER = FLOOR_NUMBER.substring(0, 1);
        }
    }
}

package com.example.mapki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SavedPlaces extends AppCompatActivity {
    public static ArrayList<LatLng> savedLoc = new ArrayList<>();
    public static ArrayList<String> savedName = new ArrayList<>();
    String All= "";
    TextView text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        text = findViewById(R.id.text);

        if ( savedLoc.size() == 0){
            text.setText("No places are saved...");
        }else {
        for(int i = 0; i <savedName.size(); i++)
        {All =All + savedName.get(i)+ "   "+savedLoc.get(i)+"                                                       ";
            text.setText(All);
        }}

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

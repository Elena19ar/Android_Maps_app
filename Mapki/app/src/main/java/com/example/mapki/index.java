package com.example.mapki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class index extends AppCompatActivity {

    public static int style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


    }


    public void Retro(View view){
        Intent intent= new Intent(this, MapsActivity.class);
        startActivity(intent);
        index.style=1;
    }

    public void Night(View view){
        Intent intent= new Intent(this, MapsActivity.class);
        startActivity(intent);
        index.style=2;
    }


    public void Standard(View view){
        Intent intent= new Intent(this, MapsActivity.class);
        startActivity(intent);
        index.style=3;
    }

    public void Silver(View view){
        Intent intent= new Intent(this, MapsActivity.class);
        startActivity(intent);
        index.style=4;
    }

}

package com.example.maxim.germanlearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SelectModeActivicty extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        ((Button) findViewById(R.id.newSuit)).setOnClickListener(this);
        ((Button) findViewById(R.id.loadSuit)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        switch(v.getId()){
            case R.id.loadSuit:
                intent.putExtra("loadExistingSuite", true);
                break;
            case R.id.newSuit:
                intent.putExtra("loadExistingSuite", false);
                break;
        }
        startActivity(intent);
    }

}

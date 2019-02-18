package com.example.myp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    Button botonCrearAula;
    //Intent homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        botonCrearAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
             startActivity(homeActivity);
             finish();
            }
        });
    }
}

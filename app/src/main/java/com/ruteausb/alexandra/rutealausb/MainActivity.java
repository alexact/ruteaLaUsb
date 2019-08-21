package com.ruteausb.alexandra.rutealausb;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Button btncomienzo;
    Grafo grafo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btncomienzo = (Button) findViewById(R.id.bcomenzar);

        btncomienzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    grafo=new Grafo();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent comenzar = new Intent(getApplicationContext(), Contain_Menu.class);
                startActivity(comenzar);
            }
        });

    }

}

package com.example.neveralone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.neveralone.R;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Button comienza1 = findViewById(R.id.comienza1);
        /*TODO aqui se tiene que decidir si hay que iniciar sesión o hay que mostrar la pantalla principal del usuario
         *  Y hay que cambiar el manifest para que apunte a esta pantalla PRIMERO*/
        comienza1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish() ;
            }

        });
    }
}
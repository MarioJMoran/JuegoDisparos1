package com.w0w23gaming.juegodisparos1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        //TIEMPO DEL SPLASH - EQUIVALE A 3S
        int DURACION_SPLASH = 3000;

        //El new Handler nos permite ejecutar una linea de código en un tiempo determinado.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //NOS PERMITE IR DE UNA ACTIVIDAD A OTRA
                Intent intent = new Intent(Splash.this,Menu.class);
                startActivity(intent);
                finish();
            }
        },DURACION_SPLASH);
    }
}







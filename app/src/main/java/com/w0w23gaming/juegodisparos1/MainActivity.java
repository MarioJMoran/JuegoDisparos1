package com.w0w23gaming.juegodisparos1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button BTNLOGIN, BTNREGISTRO;
    MediaPlayer chewbacca,inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chewbacca = MediaPlayer.create(this,R.raw.chewbacca);
        inicio = MediaPlayer.create(this, R.raw.inicio);
        inicio.start();
        BTNLOGIN = findViewById(R.id.BTNLOGIN);
        BTNREGISTRO = findViewById(R.id.BTNREGISTRO);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(MainActivity.this.getAssets(),ubicacion);

        //CAMBIAR TIPO DE LETRA
        BTNREGISTRO.setTypeface(Tf);
        BTNLOGIN.setTypeface(Tf);

        //LE DAMOS FUNCIONALIDAD AL BOTON LOGIN
        BTNLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chewbacca.start();
                inicio.stop();
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
        //LE DAMOS FUNCIONALIDAD AL BOTON REGISTRO
        BTNREGISTRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chewbacca.start();
                inicio.stop();
                startActivity(new Intent(MainActivity.this,Registro.class));
            }
        });
    }
}
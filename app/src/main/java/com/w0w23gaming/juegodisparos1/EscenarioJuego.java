package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {

    String UIDS, NICKS, NAVES;
    TextView TvContador, TvNombre, TvTiempo;
    ImageView TvNave;

    RelativeLayout relativeLayout;

    //ESTE ES EL CONTADOR A USAR EN TVCONTADOR
    int contador = 0;

    //TAMAÑO VENTANA
    int AnchoPantalla, Altopantalla;
    TextView AnchoTv, AltoTv;

    //MOVIMIENTO NAVE
    Random aleatorio;

    //PANTALLA GAMEOVER
    boolean GameOver = false;
    Dialog miDialog;

    //FIREBASE
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    //MEDIAPLAYER
    MediaPlayer jugarbatalla, blaster, respiracionvather;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);
        //PARA QUE AL GIRAR EL DISPOSITIVO NO SE REINICIE EL CONTADOR
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);
        TvNave = findViewById(R.id.TvNave);
        AnchoTv = findViewById(R.id.AnchoTV);
        AltoTv = findViewById(R.id.AltoTV);

        jugarbatalla = MediaPlayer.create(this, R.raw.jugarbatalla);
        jugarbatalla.start();
        blaster = MediaPlayer.create(this,R.raw.blaster);
        respiracionvather = MediaPlayer.create(this, R.raw.respiracionvather);

        relativeLayout = findViewById(R.id.relativeLayout);


        miDialog = new Dialog(EscenarioJuego.this);

        //INICIALIZAMOS LA BASE DE DATOS DE FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("BD JUGADORES");

        //PARA RECUPERAR DATOS DE OTRA CLASE USAMOS UN BUNDLE (EN ESTE CASO DESDE MENÚ)
        Bundle intent = getIntent().getExtras();
        UIDS = intent.getString("UID");
        NICKS = intent.getString("NICK");
        NAVES = intent.getString("NAVES");

        //SETEAMOS LOS DATOS
        TvContador.setText(NAVES);
        TvNombre.setText(NICKS);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(EscenarioJuego.this.getAssets(),ubicacion);


        //CAMBIAR TIPO DE LETRA
        TvNombre.setTypeface(Tf);
        TvContador.setTypeface(Tf);
        TvTiempo.setTypeface(Tf);


        Pantalla();
        CuentaAtras();
        EventoContador();

        //AL HACER CLICK EN LA IMAGEN
        TvNave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blaster.start();
                if (!GameOver){
                //CADA VEZ QUE HAGAMOS CLICK VA A AUMENTAR EL CONTADOR Y LO PASAMOS A STRING
                contador++;
                TvContador.setText(String.valueOf(contador));

                //CAMBIAR DE IMAGEN CADA VEZ QUE HAGAMOS CLICK EN ESTA IMAGEN
                TvNave.setImageResource(R.drawable.explode);


                //NOS PERMITE VOLVER A PONER LA IMAGEN INICIAL PASADO UN TIEMPO
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        TvNave.setImageResource(R.drawable.nave1);
                        Movimiento();
                    }
                },600);

            }}
        });




    }

    private void EventoContador(){
        if(contador>=15){
            relativeLayout.setBackgroundResource(R.drawable.escenario2);
    }}

    //METODO PARA LA OBTENCION DEL TAMAÑO DE LA PANTALLA
    private void Pantalla(){

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        AnchoPantalla = point.x;
        Altopantalla = point.y;

        //PASAMOS LOS VALORES A STRING
        String ANCHOS = String.valueOf(AnchoPantalla);
        String ALTOS = String.valueOf(Altopantalla);

        //SETEAMOS LA CADENA
        AnchoTv.setText(ANCHOS);
        AltoTv.setText(ALTOS);

        aleatorio = new Random();
    }

    //METODO PARA EL MOVIMIENTO DE LA NAVE
    private void Movimiento(){

        int min = 0;

        //MAXIMO DE COORDENADAS X Y
        int MaximoX = AnchoPantalla - TvNave.getWidth();
        int MaximoY = Altopantalla - TvNave.getHeight();

        int randomX = aleatorio.nextInt(((MaximoX - min)+1)+min);
        int randomY = aleatorio.nextInt(((MaximoY - min)+2)+min);

        TvNave.setX(randomX);
        TvNave.setY(randomY);

    }

    //METODO PARA LA CUENTA ATRÁS
    private void CuentaAtras(){
        new CountDownTimer(30000,1000){

            //SE EJECUTA CADA SEGUNDO
            public void onTick(long millisUntilFinished){
                long segundosRestantes = millisUntilFinished/1000;
                TvTiempo.setText(segundosRestantes+" S");
            }
            //CUANDO SE ACABA EL TIEMPO
            public void onFinish(){
                TvTiempo.setText("0 S");
                GameOver = true;
                MensajeGameOver();
                GuardarResultados("Naves",contador);
            }
        }.start();
    }

    //METODO PARA EL GAMEOVER
    private void MensajeGameOver(){

        jugarbatalla.stop();
        respiracionvather.start();

        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface typeface = Typeface.createFromAsset(EscenarioJuego.this.getAssets(),ubicacion);

        TextView Fin, killsTxt, NumeroTxt;
        Button PlayAgain, irMenu, Puntuacion;

        //CONEXION CON CLASE GAMEOVER.XML
        miDialog.setContentView(R.layout.gameover);
        //INICIALIZACION DE VARIABLES
        Fin = miDialog.findViewById(R.id.Fin);
        killsTxt = miDialog.findViewById(R.id.killsTxt);
        NumeroTxt = miDialog.findViewById(R.id.NumeroTxt);
        PlayAgain = miDialog.findViewById(R.id.PlayAgain);
        irMenu = miDialog.findViewById(R.id.irMenu);
        Puntuacion = miDialog.findViewById(R.id.Puntuacion);

        //VISUALIZACION PUNTUACION EN PANTALLA GAMEOVER
        String naves = String.valueOf(contador);
        NumeroTxt.setText(naves);

        //LE DAMOS FORMATO A LA FUENTE
        Fin.setTypeface(typeface);
        killsTxt.setTypeface(typeface);
        NumeroTxt.setTypeface(typeface);
        PlayAgain.setTypeface(typeface);
        irMenu.setTypeface(typeface);
        Puntuacion.setTypeface(typeface);

        PlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respiracionvather.stop();
                //Toast.makeText(EscenarioJuego.this,"PLAY AGAIN",Toast.LENGTH_SHORT).show();
                contador = 0;
                miDialog.dismiss();
                TvContador.setText("0");
                GameOver = false;
                CuentaAtras();
                Movimiento();
            }
        });

        irMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respiracionvather.stop();
                startActivity(new Intent(EscenarioJuego.this, Menu.class));
            }
        });

        Puntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respiracionvather.stop();
                startActivity(new Intent(EscenarioJuego.this, Ranking.class));
                //Toast.makeText(EscenarioJuego.this,"PUNTUACION",Toast.LENGTH_SHORT).show();
            }
        });

        miDialog.show();
        miDialog.setCancelable(false);

    }
    //METODO PARA GUARDAR LA PUNTUACIÓN
    private void GuardarResultados(String key, int naves){

        HashMap <String,Object> hashMap = new HashMap<>();
        hashMap.put(key,naves);
        JUGADORES.child(user.getUid()).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EscenarioJuego.this,"LA PUNTUACIÓN HA SIDO ACTUALIZADA",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    //POR SI LE DAS AL BOTON ATRAS SIN QUERER Y NO QUEREMOS QUE FUNCIONE
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
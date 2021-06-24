package com.w0w23gaming.juegodisparos1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetalleJugador extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView InformacionDetalle, NombreDetalle, CorreoDetalle, PuntuacionDetalle, PaisDetalle,FechaDetalle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_jugador);

        //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        InformacionDetalle = findViewById(R.id.InformacionDetalle);
        NombreDetalle = findViewById(R.id.NombreDetalle);
        CorreoDetalle = findViewById(R.id.CorreoDetalle);
        PuntuacionDetalle = findViewById(R.id.PuntuacionDetalle);
        PaisDetalle = findViewById(R.id.PaisDetalle);
        FechaDetalle = findViewById(R.id.FechaDetalle);

        //COGEMOS LOS DATOS DE LA CLASE ADDAPTADOR
        String imagen = getIntent().getStringExtra("Imagen");
        String Correo = getIntent().getStringExtra("Correo");
        String Puntuacion = getIntent().getStringExtra("Puntuacion");
        String Pais = getIntent().getStringExtra("Pais");
        String Nombre = getIntent().getStringExtra("Nombre");
        String Fecha = getIntent().getStringExtra("Fecha");

        NombreDetalle.setText(Nombre);
        PuntuacionDetalle.setText(Puntuacion);
        PaisDetalle.setText(Pais);
        FechaDetalle.setText(Fecha);
        CorreoDetalle.setText(Correo);


        //GESTION DE IMAGEN
        try {
            Picasso.get().load(imagen).into(ImagenDetalle);
        }catch (Exception e){
            Picasso.get().load(R.drawable.oscuro).into(ImagenDetalle);
        }
        CambioFuente();
    }


    //METODO PARA CAMBIAR LA FUENTE
    private void CambioFuente(){

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(DetalleJugador.this.getAssets(),ubicacion);


        //CAMBIAR TIPO DE LETRA
        InformacionDetalle.setTypeface(Tf);
        NombreDetalle.setTypeface(Tf);
        PuntuacionDetalle.setTypeface(Tf);
        PaisDetalle.setTypeface(Tf);
        FechaDetalle.setTypeface(Tf);

    }
}
package com.w0w23gaming.juegodisparos1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder>{

    private Context context;
    private List <Usuario> usuarioList;
    private Typeface Tf;

    //CONSTRUCTOR
    public Adaptador(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
        this.Tf = Typeface.createFromAsset(context.getAssets(),"fuentes/Starjedi.ttf");
    }

    @Override
    //INYECTAMOS EL DISEÑO
    public MyHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jugadores,parent,false);
        return new MyHolder(view);
    }

    @Override
    //OBTENCION DATOS DEL MODELO, CLASE USUARIO
    public void onBindViewHolder(@NonNull Adaptador.MyHolder holder, int i) {

        final String Imagen = usuarioList.get(i).getImagen();
        final String Nombre = usuarioList.get(i).getNick();
        final String Pais = usuarioList.get(i).getPais();
        final String Correo = usuarioList.get(i).getEmail();
        final String Fecha = usuarioList.get(i).getFecha_Registro();
        int Naves = usuarioList.get(i).getNaves();
        //DEBEMOS CONVERTIR LA PUNTUACION QUE ES ENTERO EN UN STRING
        final String NaS = String.valueOf(Naves);

        //SETEO DATOS
        holder.NombreJugador.setText(Nombre);
        holder.PaisJugador.setText(Pais);
        holder.PuntuacionJugador.setText(NaS);
        holder.CorreoJugador.setText(Correo);
        holder.FechaJugador.setText(Fecha);

        //CAMBIO TIPO DE LETRA
        holder.NombreJugador.setTypeface(Tf);
        holder.PaisJugador.setTypeface(Tf);
        holder.PuntuacionJugador.setTypeface(Tf);
        holder.CorreoJugador.setTypeface(Tf);
        holder.FechaJugador.setTypeface(Tf);

        //SETEO IMAGEN
        try{
            //SI TIENE FOTO DE PERFIL SE CARGA NORMAL
            Picasso.get().load(Imagen).into(holder.ImagenJugador);
        }catch (Exception e){
            //SI NO TIENE FOTO DE PERFIL
        }

        //AL HACER CLICK EN UN JUGADOR
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetalleJugador.class);

                intent.putExtra("Imagen",Imagen);
                intent.putExtra("Correo",Correo);
                intent.putExtra("Puntuacion",NaS);
                intent.putExtra("Pais",Pais);
                intent.putExtra("Nombre",Nombre);
                intent.putExtra("Fecha",Fecha);

                context.startActivity(intent);

                Toast.makeText(context, ""+Correo, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{


        ImageView ImagenJugador;
        TextView NombreJugador, PaisJugador, PuntuacionJugador, FechaJugador, CorreoJugador;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
            ImagenJugador = itemView.findViewById(R.id.ImagenJugador);

            NombreJugador = itemView.findViewById(R.id.NombreJugador);
            PaisJugador = itemView.findViewById(R.id.PaisJugador);
            PuntuacionJugador = itemView.findViewById(R.id.PuntuacionJugador);
            FechaJugador = itemView.findViewById(R.id.FechaJugador);
            CorreoJugador = itemView.findViewById(R.id.CorreoJugador);



        }
    }

}

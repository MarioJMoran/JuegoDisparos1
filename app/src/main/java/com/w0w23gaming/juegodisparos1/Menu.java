package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    TextView Puntuaciontxt, Navestxt, uid, email, nick, Menutxt, fecha, Paistxt, PerfilTxt;
    Button  JugarBtn,RankingBtn,InfoBtn,CerrarSesionBtn, editarBtn, cambiarpassBtn;
    CircleImageView imgPerfil;

    //VARIABLES PARA EL ALMACENAMIENTO DE FOTO DE PERFIL
    private StorageReference ReferenciaDeAlmacenamiento;
    private String RutaAlmacenamiento = "FotosDePerfil/*";

    //PERMISOS
    private static final int CODIGO_SOLICITUD_ALMACENAMIENTO = 100;
    private static final int CODIGO_SELECCION_IMAGEN = 150;

    //ARRAYS
    private String [] PermisosAlmacenamiento;
    private Uri imagen_uri;
    private String perfil;

    //INFO
    Dialog dialog;

    //MEDIA
    MediaPlayer menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //INICIALIZAR LA BD
        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("BD JUGADORES");

        ReferenciaDeAlmacenamiento = FirebaseStorage.getInstance().getReference();
        PermisosAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        menu = MediaPlayer.create(this,R.raw.menu);


        //VENTANA INFO
        dialog = new Dialog(Menu.this);



        //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
        Puntuaciontxt = findViewById(R.id.Puntuaciontxt);
        Navestxt = findViewById(R.id.Navestxt);
        uid = findViewById(R.id.uid);
        email = findViewById(R.id.email);
        nick = findViewById(R.id.nick);
        fecha = findViewById(R.id.fecha);
        Menutxt = findViewById(R.id.Menutxt);
        Paistxt = findViewById(R.id.Paistxt);
        PerfilTxt = findViewById(R.id.PerfilTxt);

        imgPerfil = findViewById(R.id.imgPerfil);

        CerrarSesionBtn = findViewById(R.id.CerrarSesionBtn);
        JugarBtn = findViewById(R.id.JugarBtn);
        RankingBtn = findViewById(R.id.RankingBtn);
        InfoBtn = findViewById(R.id.InfoBtn);
        editarBtn = findViewById(R.id.editarBtn);
        cambiarpassBtn = findViewById(R.id.cambiarpassBtn);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        //CAMBIAR TIPO DE LETRA
        PerfilTxt.setTypeface(Tf);
        Puntuaciontxt.setTypeface(Tf);
        Navestxt.setTypeface(Tf);
        nick.setTypeface(Tf);
        Menutxt.setTypeface(Tf);
        CerrarSesionBtn.setTypeface(Tf);
        JugarBtn.setTypeface(Tf);
        RankingBtn.setTypeface(Tf);
        InfoBtn.setTypeface(Tf);
        fecha.setTypeface(Tf);
        Paistxt.setTypeface(Tf);
        cambiarpassBtn.setTypeface(Tf);
        editarBtn.setTypeface(Tf);

        JugarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Menu.this,"Jugar",Toast.LENGTH_SHORT).show();
                menu.stop();
                Intent intent = new Intent(Menu.this,EscenarioJuego.class);

                //DECLARAMOS COMO STRING PARA PODER MANDARLO COMO PARAMETRO A LA CLASE ESCENARIOJUEGO
                String UidS = uid.getText().toString();
                String NickS = nick.getText().toString();
                String NaveS = Navestxt.getText().toString();

                intent.putExtra("UID",UidS);
                intent.putExtra("NICK",NickS);
                intent.putExtra("NAVES",NaveS);

                startActivity(intent);
                //Toast.makeText(Menu.this, "ENVIANDO PARAMETROS", Toast.LENGTH_SHORT).show();

            }
        });

        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.stop();
                Toast.makeText(Menu.this,"editar",Toast.LENGTH_SHORT).show();
                EditarDatos();
            }
        });

        cambiarpassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.stop();
                Toast.makeText(Menu.this,"cambiar",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Menu.this, CambioPass.class));
            }
        });

        RankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.stop();
                Toast.makeText(Menu.this,"RANKING",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Menu.this, Ranking.class));
            }
        });

        InfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDe();
                Toast.makeText(Menu.this,"INFORMACION",Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.stop();
                Cerrar_Sesion();
            }
        });
    }

    private void InfoDe() {
        TextView DesarrolladoPorTxt, DesarrolladorTxt, LinkLinkedin;
        Button ok;

        dialog.setContentView(R.layout.info);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        DesarrolladoPorTxt = dialog.findViewById(R.id.DesarrolladoPorTxt);
        DesarrolladorTxt = dialog.findViewById(R.id.DesarrolladorTxt);
        LinkLinkedin = dialog.findViewById(R.id.LinkLinkedin);
        ok = dialog.findViewById(R.id.ok);

        //HYPERLINK LINKEDIN
        LinkLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkLinkedin.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

        //PARA SALIR DEL CUADRO DE DIALOG
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        DesarrolladoPorTxt.setTypeface(Tf);
        DesarrolladorTxt.setTypeface(Tf);
        LinkLinkedin.setTypeface(Tf);
        ok.setTypeface(Tf);

    }

    //METODO PARA EDITAR DATOS
    //ABRIMOS UNA VENTANA NUEVA; CON UN MENU, EN EL CUAL, LAS OPCIONES, SON LAS QUE USAMOS EN EL ARRAY
    private void EditarDatos() {
        //DEFINIR UN ARRAY CON LAS OPCIONES A EDITAR
        String [] Opciones = {"Foto de perfil","Cambiar País","Cambiar Alias"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(Opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i==0){
                    perfil = "Imagen";
                    ActualizarFotoPerfil();
                }
                if (i==1){
                    ActualizarPais("Pais");
                }
                if (i==2){
                    ActualizarNick("Nick");
                }

            }
        });
        //PARA VISUALIZAR EL BUILDER, QUE ES LO QUE USAREMOS PARA LA EDICION DE DATOS
        builder.create().show();
    }

    //METODO PARA ACTUALIZAR EL NICK
    private void ActualizarNick(final String key) {
        menu.stop();
        //CREAMOS UNA VENTANA DE ALERTA EN EL CUAL VAMOS A TENER TODA LA INTERFAZ PARA CAMBIAR
        //EL NICK EN ESTE CASO
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Escribe "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //SI EL USUARIO HACE CLICK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //AQUI LO QUE HACEMOS ES COGER EL VALOR QUE HA PUESTO EL USUARIO, PASARLO A STRING
                //QUITARLE TODOS LOS ESPACIOS POR SI ACASO Y REEMPLAZARLO POR EL VALOR QUE TENEMOS
                //EN LA BD EN EL CAMPO NICK
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "NICK ACTUALIZADO", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, "FALLO AL ACTUALIZAR NICK", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Menu.this, "CANCELADO POR EL JUGADOR", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void ActualizarPais(final String key) {
        menu.stop();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        EditText editText = new EditText(this);
        editText.setHint("Escribe "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Menu.this, "PAIS ACTUALIZADO", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, "FALLO AL ACTUALIZAR PAIS", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Menu.this, "CANCELADO POR EL JUGADOR", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void ActualizarFotoPerfil() {
        menu.stop();
        String [] opciones = {"Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int i) {
           
                if( i == 0){
                    //SELECIONAMOS GALERIA
                    if (!ComprobarPermisoAlmacenamiento()){
                        //SI NO SE HABILITA EL PERMISO
                        SolicitarPermisoAlmacenamiento();
                    }else{
                        //SI SE HABILITA EL PERMISO
                        ElegirImagenGaleria();
                    }
                }
                
            }
        });
        builder.create().show();
    }

    //COMPRUEBA SI LOS PERMISOS DE ALMACENAMIENTO ESTAN HABILITADOS
    private boolean ComprobarPermisoAlmacenamiento() {

        boolean resultado = ContextCompat.checkSelfPermission(Menu.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;

    }

    //SE LE LLAMA CUANDO EL JUGADOR PRESIONA PERMITIR O DENEGAR EL CUADRO DE ALMACENAMIENTO EXTERNO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case CODIGO_SOLICITUD_ALMACENAMIENTO:{
            //SELECCION DE GALERIA
                if (grantResults.length > 0){
                    boolean EscrituraDeAlmacenamientoAceptado = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (EscrituraDeAlmacenamientoAceptado){
                        //PERMISO HABILITADO
                        ElegirImagenGaleria();
                    }else{
                        //SI EL USUARIO DIJO QUE NO
                        Toast.makeText(this, "HABILITAR PERMISO ACCESO GALERIA ", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //SE LLAMA CUANDO EL JUGADOR HA ELEGIDO LA IMAGEN DE LA GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            //OBTENEMOS LA URI DE LA IMAGEN
            if (requestCode == CODIGO_SELECCION_IMAGEN){
                imagen_uri = data.getData();
                SubirFoto(imagen_uri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //CAMBIA LA FOTO DE PERFIL DE UN JUGADOR Y SE ACTUALIZA EN LA BD DE FIREBASE
    private void SubirFoto(Uri imagen_uri) {

        String RutaArchivoYNombre = RutaAlmacenamiento + "" + perfil + "" +user.getUid();
        StorageReference storageReference = ReferenciaDeAlmacenamiento.child(RutaArchivoYNombre);
        storageReference.putFile(imagen_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloaduri = uriTask.getResult();
                            if (uriTask.isSuccessful()){
                                HashMap<String,Object> resultado = new HashMap<>();
                                resultado.put(perfil,downloaduri.toString());
                                JUGADORES.child(user.getUid()).updateChildren(resultado)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(Menu.this, "IMAGEN CAMBIADA CON EXITO", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Menu.this, "OCURRIO UN ERROR", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else {
                                Toast.makeText(Menu.this, "ALGO SALIO MAL", Toast.LENGTH_SHORT).show();
                            }
                        }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Menu.this, "ALGO SALIO MAL", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //METODO PARA ABRIR LA GALERIA
    private void ElegirImagenGaleria() {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK);
        intentGaleria.setType("image/*");
        startActivityForResult(intentGaleria,CODIGO_SELECCION_IMAGEN);
    }

    //PEDIR PERMISO DE ALMACENAMIENTO
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SolicitarPermisoAlmacenamiento() {
        requestPermissions(PermisosAlmacenamiento,CODIGO_SOLICITUD_ALMACENAMIENTO);
    }


    //METODO SE EJECUTA CUANDO SE ABRE EL JUEGO
    @Override
    protected void onStart() {
        UsuarioLogueado();

        super.onStart();
    }

    //METODO PARA COMPORBAR QUE EL JUGADOR TIENE INICIADA SESION
    private void UsuarioLogueado(){
        if (user != null){
            Consulta();
            menu.start();
            Toast.makeText(this, "Jugador conectado", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(Menu.this, MainActivity.class));
            finish();
        }

    }
    //METODO PARA CERRAR SESION
    private void Cerrar_Sesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(this,"Sesión Cerrada",Toast.LENGTH_SHORT).show();
    }

    //CONSULTA EN LA BD
    private void Consulta(){
        //SE ORDENA POR EMAIL Y SE BUSCA EL CORREO DEL USER
        Query query = JUGADORES.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    //OBTENCION DE DATOS
                    String naveString = ""+ds.child("Naves").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String emailString = ""+ds.child("Email").getValue();
                    String nickString = ""+ds.child("Nick").getValue();
                    String fechaString = ""+ds.child("Fecha_Registro").getValue();
                    String paisString = ""+ds.child("Pais").getValue();
                    String imagen = ""+ds.child("Imagen").getValue();
                    

                    //SETEO DE DATOS EN TEXTVIEW
                    Navestxt.setText(naveString);
                    uid.setText(uidString);
                    email.setText(emailString);
                    nick.setText(nickString);
                    fecha.setText(fechaString);
                    Paistxt.setText(paisString);

                    //ESTABLECER IMAGEN PERFIL
                    //DADO QUE LA PRIMERA VEZ NO HABREMOS PUESTO UNA IMAGEN DE PERFIL, NOS DARIA ERROR
                    //DE AHI METERLO EN UN TRY-CATCH Y PONER QUE SI HAY ALGUN FALLO PONGA UNA IMAGEN QUE
                    //NOSOTROS QUEREMOS
                    try {
                        Picasso.get().load(imagen).into(imgPerfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.oscuro).into(imgPerfil);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
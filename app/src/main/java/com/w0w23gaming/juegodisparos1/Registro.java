package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {

    //DECLARACIÓN DE VARIABLES
    EditText emailEt, passwordEt, nombreEt, Pais;
    TextView fechaTxt;
    Button Registrar;
    MediaPlayer r2d2;

    FirebaseAuth auth;//VARIABLE PARA LA AUTENTIFICACIÓN DE FIREBASE

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        r2d2 = MediaPlayer.create(this,R.raw.r2d2);

        //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        nombreEt = findViewById(R.id.nombreEt);
        fechaTxt = findViewById(R.id.fechaTxt);
        Registrar = findViewById(R.id.Registrar);
        Pais = findViewById(R.id.Pais);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(Registro.this.getAssets(),ubicacion);

        //CAMBIAR TIPO DE LETRA
        fechaTxt.setTypeface(Tf);
        Registrar.setTypeface(Tf);

        //CONEXION FIREBASE AUTH
        auth = FirebaseAuth.getInstance();

        //COMO COGER LA FECHA ACTUAL
        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);

        //COMO HACER EL REGISTRO
        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2d2.start();
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                //VALIDACIÓN DE CORREO
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEt.setError("Correo Invalido");
                    emailEt.setFocusable(true);
                }else if(password.length()<6){
                    passwordEt.setError("Contraseña debe ser mayor a 6");
                    emailEt.setFocusable(true);
                }else{
                    RegistrarJugador(email, password);
                }
            }


        });

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setMessage("REGISTRANDO");
        progressDialog.setCancelable(false);

    }
    //MÉTODO PARA REGISTRAR UN JUGADOR EN FIREBASE
    private void RegistrarJugador(String email, String password) {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //SI EL JUGADOR HA SIDO REGISTRADO CORRECTAMENTE
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();

                            int contador = 0;

                            //PARA DAR UNA IDENTIDAD AL USUARIO
                            assert user != null;

                            String uidString = user.getUid();
                            String emailString = emailEt.getText().toString();
                            String passString = passwordEt.getText().toString();
                            String nickString = nombreEt.getText().toString();
                            String paisString = Pais.getText().toString();
                            String fechaString = fechaTxt.getText().toString();

                            //AHORA USAMOS UN HASHMAP PARA PODER USAR LOS VALORES DE CADA JUGADOR
                            HashMap<Object,Object> DatosJUGADOR = new HashMap<>();
                            DatosJUGADOR.put("Uid", uidString);
                            DatosJUGADOR.put("Email", emailString);
                            DatosJUGADOR.put("Pass", passString);
                            DatosJUGADOR.put("Nick", nickString);
                            DatosJUGADOR.put("Pais", paisString);
                            DatosJUGADOR.put("Imagen","");
                            DatosJUGADOR.put("Fecha_Registro", fechaString);
                            DatosJUGADOR.put("Naves", contador);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();//INSTANCIAMOS LA BD
                            DatabaseReference reference = database.getReference("BD JUGADORES");//LE DAMOS NOMBRE A LA BD
                            reference.child(uidString).setValue(DatosJUGADOR);//LE DECIMOS QUE LOS HIJOS DE NUESTRA BD SEAN LOS ID DE LOS JUGADORES

                            startActivity(new Intent(Registro.this, Menu.class));//NADA MAS ESTAR REGISTRADO ENVIAMOS AL JUGADOR A LA PANTALLA DEL MENÚ

                            Toast.makeText(Registro.this,"USUARIO REGISTRADO",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this,"¡Ha ocurrido un error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                //POR SI FALLA EL REGISTRO, PONER UN MENSAJE
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
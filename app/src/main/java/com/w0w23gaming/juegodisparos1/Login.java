package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    //DECLARACION DE VARIABLES
    EditText emailLogin, passLogin;
    Button BotonLogin, BotonRestablecer;
    FirebaseAuth auth;

    MediaPlayer sablelaser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
        emailLogin = findViewById(R.id.emailLogin);
        passLogin = findViewById(R.id.passLogin);
        BotonLogin = findViewById(R.id.BotonLogin);
        BotonRestablecer = findViewById(R.id.BotonRestablecer);
        auth = FirebaseAuth.getInstance();

        sablelaser = MediaPlayer.create(this, R.raw.sablelaser);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(Login.this.getAssets(),ubicacion);

        //CAMBIAR TIPO DE LETRA
        BotonLogin.setTypeface(Tf);
        BotonRestablecer.setTypeface(Tf);

        //EVENTOS
        BotonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sablelaser.start();
                String email = emailLogin.getText().toString();
                String pass = passLogin.getText().toString();

                //VALIDACIÓN DEL LOGIN = QUE REGISTRO
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailLogin.setError("Correo Invalido");
                    emailLogin.setFocusable(true);
                }else if(passLogin.length()<6){
                    passLogin.setError("Contraseña debe ser mayor a 6");
                    emailLogin.setFocusable(true);
                }else{
                    LogeoDePlayer(email, pass);
                }

            }
        });

        BotonRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sablelaser.start();
                startActivity(new Intent(Login.this, ResetPass.class));
            }
        });

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("LOGUEANDO");
        progressDialog.setCancelable(false);
    }
    //METODO LOGEO JUGADOR
    private void LogeoDePlayer(String email, String pass){
        progressDialog.show();
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = auth.getCurrentUser();
                    startActivity(new Intent(Login.this, Menu.class));
                    assert user != null;//EL USUARIO NO ES NULO, LO AFIRMAMOS
                    finish();
                    Toast.makeText(Login.this,"BIENVENID@ " + user.getEmail(), Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Ha ocurrido un Error", Toast.LENGTH_SHORT).show();
                }
            }
            //SI FALLA EL LOGEO NOS MUESTRA UN MENSAJE
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
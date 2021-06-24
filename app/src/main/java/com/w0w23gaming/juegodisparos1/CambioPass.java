package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CambioPass extends AppCompatActivity {

    EditText ActualPass, NuevoPass;
    Button CambiarPass;
    DatabaseReference BASEDEDATOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pass);

        //CONEXIÓN DE VARIABLES CON SUS EQUIVALENTES EN EL LAYOUT
        ActualPass = findViewById(R.id.ActualPass);
        NuevoPass = findViewById(R.id.NuevoPass);
        CambiarPass = findViewById(R.id.CambiarPass);

        //INICIALIZAR LA BD
        BASEDEDATOS = FirebaseDatabase.getInstance().getReference("BD JUGADORES");
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //FUNCIONALIDAD DEL BOTON
        CambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ACTUAL = ActualPass.getText().toString().trim();
                String NUEVA = NuevoPass.getText().toString().trim();

                if (TextUtils.isEmpty(ACTUAL)){
                    Toast.makeText(CambioPass.this, "Llenar campo actual contraseña", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVA)){
                    Toast.makeText(CambioPass.this, "Llenar campo nueva contraseña", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(ACTUAL) && !TextUtils.isEmpty(NUEVA) && ACTUAL.length() >= 6 && NUEVA.length() >= 6){
                    CambioPassJugador(ACTUAL,NUEVA);
                }
            }
        });

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(CambioPass.this.getAssets(),ubicacion);

        //CAMBIO TIPO DE LETRA
        CambiarPass.setTypeface(Tf);
    }

    private void CambioPassJugador(String actual, String nueva) {

        AuthCredential authCredential = EmailAuthProvider.getCredential((user.getEmail()),actual);

        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                user.updatePassword(nueva).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String value = NuevoPass.getText().toString().trim();
                        HashMap<String,Object> result = new HashMap<>();
                        result.put("Pass",value);
                        BASEDEDATOS.child(user.getUid()).updateChildren(result)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CambioPass.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        firebaseAuth.signOut();
                        startActivity(new Intent(CambioPass.this, Login.class));
                        Toast.makeText(CambioPass.this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambioPass.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CambioPass.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
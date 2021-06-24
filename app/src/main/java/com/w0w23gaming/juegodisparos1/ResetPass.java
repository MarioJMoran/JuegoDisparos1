package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {

    EditText resetPass;
    Button resetBtn;
    String email;

    FirebaseAuth firebaseAuth;

    ProgressDialog mDialog;

    MediaPlayer c3po;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        c3po = MediaPlayer.create(this, R.raw.c3po);

        resetPass = findViewById(R.id.resetPass);
        resetBtn = findViewById(R.id.resetBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        //UBICACION TIPO LETRA
        String ubicacion = "fuentes/Starjedi.ttf";
        Typeface Tf = Typeface.createFromAsset(ResetPass.this.getAssets(),ubicacion);

        //CAMBIAR TIPO DE LETRA
        resetBtn.setTypeface(Tf);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c3po.start();
                email = resetPass.getText().toString().trim();

                if (!email.isEmpty()){
                    mDialog.setMessage("Espere un momento");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                }else {
                    Toast.makeText(ResetPass.this, "Debe ingresar un email", Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();

            }
        });

    }

    private void resetPassword() {

        firebaseAuth.setLanguageCode("es");
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetPass.this, "Se envió un correo para restablecer contraseña", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ResetPass.this, "No se pudo enviar el correo de restablecer contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
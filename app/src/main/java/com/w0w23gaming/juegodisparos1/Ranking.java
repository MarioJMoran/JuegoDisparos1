package com.w0w23gaming.juegodisparos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends AppCompatActivity {

    LinearLayoutManager mLayaoutManager;
    RecyclerView recyclerViewUsuarios;
    Adaptador adaptador;
    List<Usuario> usuarioList;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ranking");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        //INICIALIZAMOS LA BASE DE DATOS
        mLayaoutManager = new LinearLayoutManager(this);
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);

        //ORDENACION LISTA RANKING
        mLayaoutManager.setReverseLayout(true);
        mLayaoutManager.setStackFromEnd(true);
        recyclerViewUsuarios.setHasFixedSize(true);
        recyclerViewUsuarios.setLayoutManager(mLayaoutManager);
        usuarioList = new ArrayList<>();

        ObtenerTodosUsuarios();

    }

    private void ObtenerTodosUsuarios() {

        //PARA OBTENER EL USUARIO ACTUAL
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BD JUGADORES");
        ref.orderByChild("Naves").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarioList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //DENTRO DE LA CLASE USUARIO
                    Usuario usuario = ds.getValue(Usuario.class);

                    //SI QUEREMOS QUE NUESTRO USUARIO NO SALGA EN LA LISTA
                   /* if (!usuario.getUid().equals(fUser.getUid())){
                        usuarioList.add(usuario);
                    }*/

                    usuarioList.add(usuario);
                    adaptador = new Adaptador(Ranking.this, usuarioList);
                    recyclerViewUsuarios.setAdapter(adaptador);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

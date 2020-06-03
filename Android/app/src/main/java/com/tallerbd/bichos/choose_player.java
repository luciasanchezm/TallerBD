package com.tallerbd.bichos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class choose_player extends AppCompatActivity {

    private String currentProcess = "";
    public String toChoose = "";
    private int step = 1;

    private ArrayList<Usuario> usuarios;
    private ArrayList<Bicho> bichos;

    private RecyclerView playersRV;
    private RecyclerView.Adapter playersAdapter;
    private RecyclerView.LayoutManager playersLM;

    private RecyclerView bichosRV;
    private RecyclerView.Adapter bichosAdapter;
    private RecyclerView.LayoutManager bichosLM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_player);

        currentProcess = getIntent().getExtras().getString("Process");

        ///////////// Recycler View Stuff BICHOS
        bichos = ConnectionSQL.getBichosSinPropietario();
        bichosRV = (RecyclerView) findViewById(R.id.chooseBichoRV);
        bichosLM = new LinearLayoutManager(this);
        bichosRV.setLayoutManager(bichosLM);
        bichosAdapter = new bichoAdapter(bichos, this, step);
        bichosRV.setAdapter(bichosAdapter);

        ///////////// Recycler View Stuff PLAYERS
        usuarios = ConnectionSQL.getUsuarios();
        playersRV = (RecyclerView) findViewById(R.id.choosePlayerRV);
        playersLM = new LinearLayoutManager(this);
        playersRV.setLayoutManager(playersLM);
        playersAdapter = new usuarioAdapter(usuarios, this, step);
        playersRV.setAdapter(playersAdapter);


        CardView card = findViewById(R.id.chooseBtnCard);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentProcess.equals("Recolectar")) {
                    if(toChoose.equals("Usuario")) {
                        toChoose = "Bicho";
                        loadContent();
                    } else {
                        Log.d("Lu", "LALALA");
                        try {
                            if (ConnectionSQL.Recolectar()){
                                // show "recolectado" view
                                Log.d("Lu","recolectó");
                            } else {
                                // show "error" view
                                Log.d("Lu","error : recolectó");
                            }
                        }catch (Exception E){
                            Log.d("Lu", "Erroorrrr:" + E.getMessage());
                        }

                    }
                } else {
                    if(toChoose.equals("Usuario")) {
                        toChoose = "Bicho";
                        loadContent();
                    } else if(toChoose.equals("Bicho")) {
                        step++;
                        toChoose = "Usuario";
                        if(step < 3) {
                            loadContent();
                            return;
                        }
                        Log.d("Lu", "QUE PEX");
                        if (currentProcess.equals("Combatir")) {
                            if (ConnectionSQL.Combatir()){
                                // show "recolectado" view
                                Log.d("Lu","Combatió");
                            } else {
                                // show "error" view
                                Log.d("Lu","error : combatió");
                            }
                        } else if (currentProcess.equals("Intercambiar")) {
                            if (ConnectionSQL.Intercambiar()){
                                // show "recolectado" view
                                Log.d("Lu","intercambió");
                            } else {
                                // show "error" view
                                Log.d("Lu","error : intercambió");
                            }
                        }
                    }
                }
            }
        });

        loadContent();
    }

    public void loadContent() {
        Log.d("Lu", "toChoose: " + toChoose + " -> step: " + step);
        if(step > 2) {
            return;
        }

        if(toChoose.equals("")){
            toChoose = "Usuario";
            playersRV.setVisibility(View.VISIBLE);
            bichosRV.setVisibility(View.GONE);
            setUsuariosRecycler();
        } else if(toChoose.equals("Usuario")) {
            playersRV.setVisibility(View.VISIBLE);
            bichosRV.setVisibility(View.GONE);
            setBichosRecycler();
        } else if(toChoose.equals("Bicho")) {
            playersRV.setVisibility(View.GONE);
            bichosRV.setVisibility(View.VISIBLE);
            setUsuariosRecycler();
        }
    }

    private void setBichosRecycler() {
        bichos = ConnectionSQL.getBichosSinPropietario();
        bichosAdapter.notifyDataSetChanged();
    }

    private void setUsuariosRecycler() {
        usuarios = ConnectionSQL.getUsuarios();
        playersAdapter.notifyDataSetChanged();
    }

}
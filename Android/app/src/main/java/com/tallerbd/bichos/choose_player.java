package com.tallerbd.bichos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionSet;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.transition.*;

import android.os.Build;
import androidx.transition.TransitionManager;

import java.sql.SQLException;
import java.util.ArrayList;

import static androidx.transition.TransitionSet.ORDERING_SEQUENTIAL;

public class choose_player extends AppCompatActivity {

    private String currentProcess = "";
    public String toChoose = "";
    public static int step = 1;

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
        //bichosAdapter = new bichoAdapter(bichos, this, step);
        //bichosRV.setAdapter(bichosAdapter);

        ///////////// Recycler View Stuff PLAYERS
        usuarios = ConnectionSQL.getUsuarios();
        playersRV = (RecyclerView) findViewById(R.id.choosePlayerRV);
        playersLM = new LinearLayoutManager(this);
        playersRV.setLayoutManager(playersLM);
        playersAdapter = new usuarioAdapter(usuarios, this, step);
        playersRV.setAdapter(playersAdapter);


        CardView card = findViewById(R.id.chooseBtnCard);
        final TextView appbarText = findViewById(R.id.appBarText);
        final TextView titleCard = findViewById(R.id.chooseTitleText);
        final Context context = this;
        final Intent intent = new Intent(this, MainActivity.class);

        card.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(currentProcess.equals("Recolectar")) {
                    if(toChoose.equals("Usuario")) {
                        toChoose = "Bicho";
                        appbarText.setText("Seleccionar bicho");
                        titleCard.setText("Bichos");
                        loadContent();
                    } else {
                        Log.d("Lu", "LALALA");
                        try {
                            if (ConnectionSQL.Recolectar()){
                                // show "recolectado" view
                                Log.d("Lu","recolectó");
                                CardView smsHelper = findViewById(R.id.resultCard);
                                TextView resultTV = findViewById(R.id.resultText);
                                resultTV.setText("Recolectó");

                                //animation
                                final CardView linearLayout = (CardView) findViewById(R.id.resultCard);
                                TransitionSet mStaggeredTransition = new TransitionSet();
                                final ViewGroup mSceneRoot = (ViewGroup) findViewById(R.id.jugadoresMainLayout);

                                Transition first = new Fade(Fade.OUT);
                                Transition second = new ChangeBounds();
                                Transition third = new Fade(Fade.IN);
                                second.setStartDelay(1000).addTarget(linearLayout);

                                mStaggeredTransition.setOrdering(ORDERING_SEQUENTIAL);
                                mStaggeredTransition.addTransition(third).addTransition(second).addTransition(first);
                                linearLayout.setVisibility(View.VISIBLE);
                                mSceneRoot.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(mSceneRoot, new AutoTransition().setStartDelay(1000));
                                        linearLayout.setVisibility(View.GONE);
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        startActivity(intent);
                                    }
                                }, 1000);

                            } else {
                                // show "error" view
                                Log.d("Lu","error : recolectó");
                                CardView smsHelper = findViewById(R.id.resultCard);
                                TextView resultTV = findViewById(R.id.resultText);
                                resultTV.setText("Error en recolectar");

                                //animation
                                final CardView linearLayout = (CardView) findViewById(R.id.resultCard);
                                TransitionSet mStaggeredTransition = new TransitionSet();
                                final ViewGroup mSceneRoot = (ViewGroup) findViewById(R.id.jugadoresMainLayout);

                                Transition first = new Fade(Fade.OUT);
                                Transition second = new ChangeBounds();
                                Transition third = new Fade(Fade.IN);
                                second.setStartDelay(1000).addTarget(linearLayout);

                                mStaggeredTransition.setOrdering(ORDERING_SEQUENTIAL);
                                mStaggeredTransition.addTransition(third).addTransition(second).addTransition(first);
                                linearLayout.setVisibility(View.VISIBLE);
                                mSceneRoot.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(mSceneRoot, new AutoTransition().setStartDelay(1000));
                                        linearLayout.setVisibility(View.GONE);
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }
                        }catch (Exception E){
                            Log.d("Lu", "Erroorrrr:" + E.getMessage());
                        }

                    }
                } else {
                    if(toChoose.equals("Usuario")) {
                        toChoose = "Bicho";
                        if(step == 1)
                            appbarText.setText("Seleccionar bicho 1");
                        else
                            appbarText.setText("Seleccionar bicho 2");
                        titleCard.setText("Bichos");
                        loadContent();
                    } else if(toChoose.equals("Bicho")) {
                        step++;
                        toChoose = "Usuario";
                        appbarText.setText("Seleccionar usuario 2");
                        titleCard.setText("Usuarios");
                        if(step < 3) {
                            loadContent();
                            return;
                        }
                        Log.d("Lu", "QUE PEX");
                        if (currentProcess.equals("Combatir")) {
                            if (ConnectionSQL.Combatir()){
                                // show "recolectado" view
                                Log.d("Lu","Combatió");
                                CardView smsHelper = findViewById(R.id.resultCard);
                                TextView resultTV = findViewById(R.id.resultText);
                                resultTV.setText("Combatió");

                                //animation
                                final CardView linearLayout = (CardView) findViewById(R.id.resultCard);
                                TransitionSet mStaggeredTransition = new TransitionSet();
                                final ViewGroup mSceneRoot = (ViewGroup) findViewById(R.id.jugadoresMainLayout);

                                Transition first = new Fade(Fade.OUT);
                                Transition second = new ChangeBounds();
                                Transition third = new Fade(Fade.IN);
                                second.setStartDelay(1000).addTarget(linearLayout);

                                mStaggeredTransition.setOrdering(ORDERING_SEQUENTIAL);
                                mStaggeredTransition.addTransition(third).addTransition(second).addTransition(first);
                                linearLayout.setVisibility(View.VISIBLE);
                                mSceneRoot.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(mSceneRoot, new AutoTransition().setStartDelay(1000));
                                        linearLayout.setVisibility(View.GONE);
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        startActivity(intent);
                                    }
                                }, 1000);
                            } else {
                                // show "error" view
                                Log.d("Lu","error : combatió");
                                TextView resultTV = findViewById(R.id.resultText);
                                resultTV.setText("Error en combatir");

                                //animation
                                final CardView linearLayout = (CardView) findViewById(R.id.resultCard);
                                TransitionSet mStaggeredTransition = new TransitionSet();
                                final ViewGroup mSceneRoot = (ViewGroup) findViewById(R.id.jugadoresMainLayout);

                                Transition first = new Fade(Fade.OUT);
                                Transition second = new ChangeBounds();
                                Transition third = new Fade(Fade.IN);
                                second.setStartDelay(1000).addTarget(linearLayout);

                                mStaggeredTransition.setOrdering(ORDERING_SEQUENTIAL);
                                mStaggeredTransition.addTransition(third).addTransition(second).addTransition(first);
                                linearLayout.setVisibility(View.VISIBLE);
                                mSceneRoot.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(mSceneRoot, new AutoTransition().setStartDelay(1000));
                                        linearLayout.setVisibility(View.GONE);
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }
                        } else if (currentProcess.equals("Intercambiar")) {
                            if (ConnectionSQL.Intercambiar()){
                                // show "intercambiado" view
                                Log.d("Lu","intercambió");
                                CardView smsHelper = findViewById(R.id.resultCard);
                                TextView resultTV = findViewById(R.id.resultText);
                                resultTV.setText("Intercambió");

                                //animation
                                final CardView linearLayout = (CardView) findViewById(R.id.resultCard);
                                TransitionSet mStaggeredTransition = new TransitionSet();
                                final ViewGroup mSceneRoot = (ViewGroup) findViewById(R.id.jugadoresMainLayout);

                                Transition first = new Fade(Fade.OUT);
                                Transition second = new ChangeBounds();
                                Transition third = new Fade(Fade.IN);
                                second.setStartDelay(1000).addTarget(linearLayout);

                                mStaggeredTransition.setOrdering(ORDERING_SEQUENTIAL);
                                mStaggeredTransition.addTransition(third).addTransition(second).addTransition(first);
                                linearLayout.setVisibility(View.VISIBLE);
                                mSceneRoot.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(mSceneRoot, new AutoTransition().setStartDelay(1000));
                                        linearLayout.setVisibility(View.GONE);
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        startActivity(intent);
                                    }
                                }, 1000);
                            } else {
                                // show "error" view
                                Log.d("Lu","error : intercambió");
                                CardView smsHelper = findViewById(R.id.resultCard);
                                TextView resultTV = findViewById(R.id.resultText);
                                resultTV.setText("Error en intercambiar");

                                //animation
                                final CardView linearLayout = (CardView) findViewById(R.id.resultCard);
                                TransitionSet mStaggeredTransition = new TransitionSet();
                                final ViewGroup mSceneRoot = (ViewGroup) findViewById(R.id.jugadoresMainLayout);

                                Transition first = new Fade(Fade.OUT);
                                Transition second = new ChangeBounds();
                                Transition third = new Fade(Fade.IN);
                                second.setStartDelay(1000).addTarget(linearLayout);

                                mStaggeredTransition.setOrdering(ORDERING_SEQUENTIAL);
                                mStaggeredTransition.addTransition(third).addTransition(second).addTransition(first);
                                linearLayout.setVisibility(View.VISIBLE);
                                mSceneRoot.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        TransitionManager.beginDelayedTransition(mSceneRoot, new AutoTransition().setStartDelay(1000));
                                        linearLayout.setVisibility(View.GONE);
                                        try {
                                            ConnectionSQL.Close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            Log.d("Connection", "Error: " + e.getMessage());
                                        }
                                        startActivity(intent);
                                    }
                                }, 1000);
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

        final TextView appbarText = findViewById(R.id.appBarText);
        final TextView titleCard = findViewById(R.id.chooseTitleText);

        if(toChoose.equals("")){
            toChoose = "Usuario";
            appbarText.setText("Seleccionar usuario 1");
            titleCard.setText("Usuarios");
            setUsuariosRecycler();
        } else if(toChoose.equals("Usuario")) {
            setUsuariosRecycler();
        } else if(toChoose.equals("Bicho")) {
            setBichosRecycler();
        }
    }

    private void setBichosRecycler() {
        if(currentProcess.equals("Recolectar"))
            bichos = ConnectionSQL.getBichosSinPropietario();
        else {
            bichos = ConnectionSQL.getBichosUsuario(ConnectionSQL.getSelectedUsuario(0).id);
            Log.d("Lu", "bichosusuario");
        }

        bichosAdapter = new bichoAdapter(bichos, this, step);
        bichosRV.setAdapter(bichosAdapter);
        bichosAdapter.notifyDataSetChanged();

        bichosRV.setVisibility(View.VISIBLE);
        playersRV.setVisibility(View.GONE);
    }

    private void setUsuariosRecycler() {
        usuarios = ConnectionSQL.getUsuarios();
        playersAdapter = new usuarioAdapter(usuarios, this, step);
        playersRV.setAdapter(playersAdapter);
        playersAdapter.notifyDataSetChanged();

        playersRV.setVisibility(View.VISIBLE);
        bichosRV.setVisibility(View.GONE);
    }

}
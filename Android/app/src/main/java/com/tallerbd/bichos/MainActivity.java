package com.tallerbd.bichos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Connection connection;
    private ArrayList<Bicho> bichos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectionSQL.connect();
        choose_player.step = 1;

        final Context context = this;

        CardView recolectarCard = findViewById(R.id.recolectarCard);
        recolectarCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, choose_player.class);
                intent.putExtra("Process", "Recolectar");
                startActivity(intent);
            }
        });

        CardView combatirCard = findViewById(R.id.combatirCard);
        combatirCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, choose_player.class);
                intent.putExtra("Process", "Combatir");
                startActivity(intent);
            }
        });

        CardView intercambiarCard = findViewById(R.id.intercambiarCard);
        intercambiarCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, choose_player.class);
                intent.putExtra("Process", "Intercambiar");
                startActivity(intent);
            }
        });

    }
}

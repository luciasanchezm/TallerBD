package com.tallerbd.bichos;

import android.os.StrictMode;
import android.util.Log;

import java.security.Policy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectionSQL {

    private static ArrayList<Bicho> bichos;
    private static ArrayList<Usuario> usuarios;
    private static Bicho selectedBicho;
    private static Usuario selectedUsuario;
    private static Connection connection = null;

    private ConnectionSQL() {

    }

    public static Connection connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://luciasanchezm.database.windows.net:1433;DatabaseName=bichos_de_bolsillo;user=luciasanchezm@luciasanchezm;password=tallerBD!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (Exception E) {
            Log.d("Connection", "Connection error: " + E.getMessage());
        }
        return connection;
    }

    public static Connection getConnection() {
        return connect();
    }

    public static ArrayList<Bicho> getBichos() {
        bichos = new ArrayList();
        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return null;
        } else {
            try {
                String query = "SELECT * FROM TablaBichos_V";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()) {
                    bichos.add(
                            new Bicho(
                                    rs.getInt("ID"),
                                    rs.getString("Bicho"),
                                    rs.getInt("Salud"),
                                    rs.getString("Tipo"),
                                    rs.getString("Especie"),
                                    rs.getString("Propietario")
                            )
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return bichos;
    }

    public static ArrayList<Bicho> getBichosSinPropietario() {
        bichos = new ArrayList();
        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return null;
        } else {
            try {
                String query = "SELECT * FROM BichosSinPropietario_V";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()) {
                    bichos.add(
                            new Bicho(
                                    rs.getInt("ID"),
                                    rs.getString("Nombre"),
                                    100,
                                    rs.getString("Tipo"),
                                    rs.getString("Especie"),
                                    null
                            )
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return bichos;
    }

    public static ArrayList<Bicho> getBichosUsuario(int usuario_id) {
        bichos = new ArrayList();
        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return null;
        } else {
            try {
                String query = "SELECT * FROM ";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()) {
                    bichos.add(
                            new Bicho(
                                    rs.getInt("ID"),
                                    rs.getString("Bicho"),
                                    rs.getInt("Salud"),
                                    rs.getString("Tipo"),
                                    rs.getString("Especie"),
                                    rs.getString("Propietario")
                            )
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return bichos;
    }

    public static ArrayList<Usuario> getUsuarios() {
        usuarios = new ArrayList<Usuario>();

        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return null;
        } else {
            try {
                String query = "SELECT * FROM GetJugadores_V";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while(rs.next()) {
                    usuarios.add(
                            new Usuario(
                                    rs.getInt("ID"),
                                    rs.getString("Nombre"),
                                    rs.getString("Correo")
                            )
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return usuarios;
    }

    public static void setSelectedBicho(Bicho bicho) {
        selectedBicho = bicho;
    }

    public static Bicho getSelectedBicho() {
        return selectedBicho;
    }

    public static void setSelectedUsuario(Usuario usuario) {
        selectedUsuario = usuario;
    }

    public static Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public static void Close() throws SQLException { connection.close(); }
}

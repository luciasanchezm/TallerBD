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
    private static Bicho selectedBicho1;
    private static Usuario selectedUsuario1;
    private static Bicho selectedBicho2;
    private static Usuario selectedUsuario2;
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
                String query = "SELECT * FROM GetBichosUsuario(" + (choose_player.step == 1? selectedUsuario1.id : selectedUsuario2.id) + ")";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                Log.d("Lu", "Query: " + query);
                while(rs.next()) {
                    Log.d("Lu", "bicho: " + rs.getString("Bicho"));
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
                Log.d("Connection", "Error: " + e.getMessage());
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

    public static boolean Recolectar() {
        Log.d("Lu", selectedUsuario1.id + " - " + selectedBicho1.id);
        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return false;
        } else {
            try {
                String query = "EXECUTE Recolectar " + selectedUsuario1.id + ", " + selectedBicho1.id;
                Log.d("Lu", "Query: " + query);
                Statement statement = connection.createStatement();
//                ResultSet rs = statement.executeQuery(query);
//                if(rs.next())
//                    Log.d("Lu" , "id: " + rs.getString("ID"));
                statement.execute(query);
                Log.d("Connection", "Recolectado");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d("Connection", "Error: " + e.getMessage());
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Connection", "Error: " + e.getMessage());
                return false;
            }

        }
        return true;
    }

    public static boolean Combatir() {
        Log.d("Lu", selectedUsuario1.id + " - " + selectedUsuario2.id + " - " + selectedBicho2.id + "- " + selectedBicho2.id);
        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return false;
        } else {
            try {
                String query = "EXECUTE Combatir " + selectedUsuario1.id + ", " + selectedUsuario2.id;
                Statement statement = connection.createStatement();
                statement.execute(query);
                Log.d("Connection", "Combatieron");

                // get combate id = size de la table
                query = "SELECT COUNT(combate_id) AS 'Lenght' FROM Combates ";
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                int combate = rs.getInt("Lenght");

                query = "INSERT INTO Rondas " +
                        "(combate_id, bicho_uno, bicho_dos, " +
                        "ataque_uno, ataque_dos, " +
                        "nivel_daño_uno, nivel_daño_dos) VALUES " +
                        "(" + combate + ", " + selectedBicho1.id + ", " + selectedBicho2.id +
                        ", 1, 2, 35, 20)";
                statement = connection.createStatement();
                statement.execute(query);
                Log.d("Connection", "Combatieron");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d("Connection", e.getMessage());
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Connection", e.getMessage());
                return false;
            }

        }
        return true;
    }

    public static boolean Intercambiar() {
        Log.d("Lu", selectedUsuario1.id + " - " + selectedUsuario2.id + " - " + selectedBicho2.id + "- " + selectedBicho2.id);
        if(connection == null) {
            Log.d("Connection", "Connection is null");
            return false;
        } else {
            try {
                String query = "EXECUTE Intercambiar " + selectedUsuario1.id + ", " + selectedUsuario2.id + ", " + selectedBicho1.id + ", " + selectedBicho2.id;
                Statement statement = connection.createStatement();
                statement.execute(query);
                Log.d("Connection", "Intercambiado");
            } catch (SQLException e) {
                Log.d("Connection", e.getMessage());
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Connection", e.getMessage());
                return false;
            }

        }
        return true;
    }

    public static void setSelectedBicho(Bicho bicho, int num) {
        Log.d("Lu", " setSelectedBicho step: " + choose_player.step + " - " + num);
        if(choose_player.step == 1)
            selectedBicho1 = bicho;
        else
            selectedBicho2 = bicho;
    }

    public static Bicho getSelectedBicho(int num) {
        Log.d("Lu", "getSelectedBicho step: " + choose_player.step + " - " + num);
        if(choose_player.step == 1)
            return selectedBicho1;
        else
            return selectedBicho2;
    }

    public static void setSelectedUsuario(Usuario usuario, int num) {
        Log.d("Lu", " setSelectedUsuario step: " + choose_player.step + " - " + num);
        if(choose_player.step == 1)
            selectedUsuario1 = usuario;
        else
            selectedUsuario2 = usuario;
    }

    public static Usuario getSelectedUsuario(int num) {
        Log.d("Lu", "getSelectedUsuario step: " + choose_player.step + " - " + num);
        if(choose_player.step == 1)
            return selectedUsuario1;
        else
            return selectedUsuario2;
    }

    public static void Close() throws SQLException {
        selectedUsuario1 = null;
        selectedUsuario2 = null;
        selectedBicho1 = null;
        selectedBicho2 = null;
        connection.close();
    }
}

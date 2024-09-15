import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    public static String url = "jdbc:mysql://localhost:3306/";
    public static String usuario = "root";
    public static String contrasena = "";

    public static void crearBaseDeDatos() {
        String nombreBaseDatos = JOptionPane.showInputDialog("Ingresa el nombre de la nueva base de datos:");
        if (nombreBaseDatos != null && !nombreBaseDatos.trim().isEmpty()) {
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena);
                 Statement statement = conexion.createStatement()) {

                statement.executeUpdate("CREATE DATABASE " + nombreBaseDatos);
                JOptionPane.showMessageDialog(null, "Base de datos creada exitosamente!");
                crearTablas(nombreBaseDatos);

            } catch (SQLException e) {
                mostrarError("Error al crear la base de datos.");
                e.printStackTrace();
            }
        }
    }

    public static void crearTablas(String nombreBaseDatos) {
        boolean continuar = true;
        while (continuar) {
            String nombreTabla = JOptionPane.showInputDialog("Ingresa el nombre de la nueva tabla:");
            if (nombreTabla != null && !nombreTabla.trim().isEmpty()) {
                List<String> columnas = new ArrayList<>();
                boolean agregarColumnas = true;
                while (agregarColumnas) {
                    String columna = JOptionPane.showInputDialog("Ingresa el nombre y tipo de columna (ejemplo: nombre VARCHAR(255)):");
                    if (columna != null && !columna.trim().isEmpty()) {
                        columnas.add(columna);
                    } else {
                        agregarColumnas = false;
                    }
                }

                if (!columnas.isEmpty()) {
                    try (Connection conexionBD = DriverManager.getConnection(url + nombreBaseDatos, usuario, contrasena);
                         Statement statementBD = conexionBD.createStatement()) {

                        StringBuilder query = new StringBuilder("CREATE TABLE " + nombreTabla + " (");
                        for (int i = 0; i < columnas.size(); i++) {
                            query.append(columnas.get(i));
                            if (i < columnas.size() - 1) {
                                query.append(", ");
                            }
                        }
                        query.append(");");
                        statementBD.executeUpdate(query.toString());
                        JOptionPane.showMessageDialog(null, "Tabla creada exitosamente!");

                    } catch (SQLException e) {
                        mostrarError("Error al crear la tabla.");
                        e.printStackTrace();
                    }
                }
            }

            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas agregar otra tabla?", "Agregar otra tabla", JOptionPane.YES_NO_OPTION);
            if (opcion != JOptionPane.YES_OPTION) {
                continuar = false;
            }
        }
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void agregarTablas(String nombreBaseDatos) {
        boolean continuar = true;
        while (continuar) {
            String nombreTabla = JOptionPane.showInputDialog("Ingresa el nombre de la nueva tabla:");
            if (nombreTabla != null && !nombreTabla.trim().isEmpty()) {
                List<String> columnas = new ArrayList<>();
                boolean agregarColumnas = true;
                while (agregarColumnas) {
                    String columna = JOptionPane.showInputDialog("Ingresa el nombre y tipo de columna (ejemplo: nombre VARCHAR(255)):");
                    if (columna != null && !columna.trim().isEmpty()) {
                        columnas.add(columna);
                    } else {
                        agregarColumnas = false;
                    }
                }

                if (!columnas.isEmpty()) {
                    try (Connection conexionBD = DriverManager.getConnection(url + nombreBaseDatos, usuario, contrasena);
                         Statement statementBD = conexionBD.createStatement()) {

                        StringBuilder query = new StringBuilder("CREATE TABLE " + nombreTabla + " (");
                        for (int i = 0; i < columnas.size(); i++) {
                            query.append(columnas.get(i));
                            if (i < columnas.size() - 1) {
                                query.append(", ");
                            }
                        }
                        query.append(");");
                        statementBD.executeUpdate(query.toString());
                        JOptionPane.showMessageDialog(null, "Tabla creada exitosamente!");

                    } catch (SQLException e) {
                        mostrarError("Error al crear la tabla.");
                        e.printStackTrace();
                    }
                }
            }

            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas agregar otra tabla?", "Agregar otra tabla", JOptionPane.YES_NO_OPTION);
            if (opcion != JOptionPane.YES_OPTION) {
                continuar = false;
            }
        }
    }

}
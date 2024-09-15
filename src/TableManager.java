import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TableManager {

    public static void mostrarTablas(String nombreBaseDatos) {
        try (Connection conexionBD = DriverManager.getConnection(DatabaseManager.url + nombreBaseDatos, DatabaseManager.usuario, DatabaseManager.contrasena);
             Statement statementBD = conexionBD.createStatement()) {

            ResultSet tablas = statementBD.executeQuery("SHOW TABLES;");
            JComboBox<String> comboBoxTablas = new JComboBox<>();
            while (tablas.next()) {
                comboBoxTablas.addItem(tablas.getString(1));
            }

            int resultado = JOptionPane.showConfirmDialog(null, comboBoxTablas, "Selecciona una tabla", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                String nombreTabla = (String) comboBoxTablas.getSelectedItem();
                mostrarContenidoTabla(nombreBaseDatos, nombreTabla);
            }

        } catch (SQLException e) {
            mostrarError("Error al conectar con la base de datos seleccionada.");
            e.printStackTrace();
        }
    }

    public static void crearTablas(String nombreBaseDatos) {
        String nombreTabla = JOptionPane.showInputDialog("Ingresa el nombre de la nueva tabla:");
        if (nombreTabla != null && !nombreTabla.trim().isEmpty()) {
            try (Connection conexionBD = DriverManager.getConnection(DatabaseManager.url + nombreBaseDatos, DatabaseManager.usuario, DatabaseManager.contrasena);
                 Statement statementBD = conexionBD.createStatement()) {

                StringBuilder queryCreateTable = new StringBuilder("CREATE TABLE " + nombreTabla + " (");
                String columnas = JOptionPane.showInputDialog("Ingresa las columnas y tipos de datos (ejemplo: id INT PRIMARY KEY, nombre VARCHAR(50)): ");
                if (columnas != null && !columnas.trim().isEmpty()) {
                    queryCreateTable.append(columnas).append(");");
                    statementBD.executeUpdate(queryCreateTable.toString());
                    JOptionPane.showMessageDialog(null, "Tabla creada exitosamente!");
                    DataEntryForm.agregarDatos(nombreBaseDatos, nombreTabla);
                }

            } catch (SQLException e) {
                mostrarError("Error al crear la tabla.");
                e.printStackTrace();
            }
        }
    }

    private static void mostrarContenidoTabla(String nombreBaseDatos, String nombreTabla) {
        String query = "SELECT * FROM " + nombreTabla + ";";

        try (Connection conexionBD = DriverManager.getConnection(DatabaseManager.url + nombreBaseDatos, DatabaseManager.usuario, DatabaseManager.contrasena);
             Statement statementBD = conexionBD.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet contenidoTabla = statementBD.executeQuery(query)) {

            ResultSetMetaData metaData = contenidoTabla.getMetaData();
            int columnas = metaData.getColumnCount();

            String[] nombresColumnas = new String[columnas];
            for (int i = 1; i <= columnas; i++) {
                nombresColumnas[i - 1] = metaData.getColumnName(i);
            }

            contenidoTabla.last();
            int filas = contenidoTabla.getRow();
            contenidoTabla.beforeFirst();

            String[][] datos = new String[filas][columnas];
            int filaActual = 0;

            while (contenidoTabla.next()) {
                for (int i = 1; i <= columnas; i++) {
                    datos[filaActual][i - 1] = contenidoTabla.getString(i);
                }
                filaActual++;
            }

            JTable table = new JTable(datos, nombresColumnas);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 300));

            int opcion = JOptionPane.showOptionDialog(null, scrollPane, "Contenido de la tabla",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Agregar Datos", "Cerrar"}, "Cerrar");

            if (opcion == 0) {
                DataEntryForm.agregarDatos(nombreBaseDatos, nombreTabla);
            }

        } catch (SQLException e) {
            mostrarError("Error al obtener el contenido de la tabla seleccionada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ListarBasesDeDatos {

    public static void mostrarBasesDeDatos() {
        try (Connection conexion = DriverManager.getConnection(DatabaseManager.url, DatabaseManager.usuario, DatabaseManager.contrasena);
             Statement statement = conexion.createStatement()) {

            ResultSet basesDeDatos = statement.executeQuery("SHOW DATABASES;");

            // Crear un combo box con las bases de datos
            JComboBox<String> comboBoxBasesDatos = new JComboBox<>();
            while (basesDeDatos.next()) {
                String baseDeDatos = basesDeDatos.getString(1);
                if (!baseDeDatos.equals("information_schema") && !baseDeDatos.equals("mysql") && !baseDeDatos.equals("performance_schema") && !baseDeDatos.equals("sys")) {
                    comboBoxBasesDatos.addItem(baseDeDatos);
                }
            }

            int resultado = JOptionPane.showConfirmDialog(null, comboBoxBasesDatos, "Selecciona una base de datos", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                String nombreBaseDatos = (String) comboBoxBasesDatos.getSelectedItem();
                mostrarTablas(nombreBaseDatos);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor MySQL.");
            e.printStackTrace();
        }
    }

    private static void mostrarTablas(String nombreBaseDatos) {
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
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos seleccionada.");
            e.printStackTrace();
        }
    }

    private static void mostrarContenidoTabla(String nombreBaseDatos, String nombreTabla) {
        String query = "SELECT * FROM " + nombreTabla + ";";

        try (Connection conexionBD = DriverManager.getConnection(DatabaseManager.url + nombreBaseDatos, DatabaseManager.usuario, DatabaseManager.contrasena);
             Statement statementBD = conexionBD.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet contenidoTabla = statementBD.executeQuery(query)) {

            ResultSetMetaData metaData = contenidoTabla.getMetaData();
            int columnas = metaData.getColumnCount();

            // Crear datos para la tabla
            String[] nombresColumnas = new String[columnas];
            for (int i = 1; i <= columnas; i++) {
                nombresColumnas[i - 1] = metaData.getColumnName(i);
            }

            contenidoTabla.last(); // Mover al último registro para contar filas
            int filas = contenidoTabla.getRow();
            contenidoTabla.beforeFirst(); // Regresar al principio para iterar

            String[][] datos = new String[filas][columnas];
            int filaActual = 0;

            while (contenidoTabla.next()) {
                for (int i = 1; i <= columnas; i++) {
                    datos[filaActual][i - 1] = contenidoTabla.getString(i);
                }
                filaActual++;
            }

            // Crear JTable con los datos
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
            JOptionPane.showMessageDialog(null, "Error al obtener el contenido de la tabla seleccionada.");
            e.printStackTrace();
        }
    }
}
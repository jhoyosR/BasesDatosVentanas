import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DataEntryForm {

    public static void agregarDatos(String nombreBaseDatos, String nombreTabla) {
        try (Connection conexionBD = DriverManager.getConnection(DatabaseManager.url + nombreBaseDatos, DatabaseManager.usuario, DatabaseManager.contrasena);
             Statement statementBD = conexionBD.createStatement()) {

            ResultSet contenidoTabla = statementBD.executeQuery("SELECT * FROM " + nombreTabla + " LIMIT 1;");
            ResultSetMetaData metaData = contenidoTabla.getMetaData();
            int columnas = metaData.getColumnCount();

            // Crear el formulario para ingresar datos
            JPanel panel = new JPanel(new GridLayout(columnas, 2, 5, 5));
            JTextField[] textFields = new JTextField[columnas];
            for (int i = 1; i <= columnas; i++) {
                String columnName = metaData.getColumnName(i);
                int columnLength = metaData.getColumnDisplaySize(i);
                JLabel label = new JLabel(columnName + " (max " + columnLength + " caracteres):");
                JTextField textField = new JTextField();
                textField.setName(columnName);
                textFields[i - 1] = textField;
                panel.add(label);
                panel.add(textField);
            }

            // Configurar la ventana del formulario
            int result = JOptionPane.showConfirmDialog(null, panel, "Ingresa los datos para la tabla", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                StringBuilder queryInsert = new StringBuilder("INSERT INTO " + nombreTabla + " (");
                for (int i = 1; i <= columnas; i++) {
                    queryInsert.append(metaData.getColumnName(i));
                    if (i < columnas) {
                        queryInsert.append(", ");
                    }
                }
                queryInsert.append(") VALUES (");
                for (int i = 1; i <= columnas; i++) {
                    queryInsert.append("?");
                    if (i < columnas) {
                        queryInsert.append(", ");
                    }
                }
                queryInsert.append(");");

                try (PreparedStatement preparedStatement = conexionBD.prepareStatement(queryInsert.toString())) {
                    for (int i = 1; i <= columnas; i++) {
                        String valor = textFields[i - 1].getText();
                        if (valor.length() > metaData.getColumnDisplaySize(i)) {
                            JOptionPane.showMessageDialog(null, "El valor para " + metaData.getColumnName(i) + " excede la longitud máxima permitida.");
                            return;
                        }
                        preparedStatement.setString(i, valor);
                    }

                    int filasInsertadas = preparedStatement.executeUpdate();
                    if (filasInsertadas > 0) {
                        JOptionPane.showMessageDialog(null, "¡Registro agregado exitosamente!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo agregar el registro.");
                    }

                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar el registro a la tabla.");
            e.printStackTrace();
        }
    }
}
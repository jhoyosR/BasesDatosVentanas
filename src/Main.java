import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        // Configuración de la ventana principal
        JFrame ventana = new JFrame("Gestión de Bases de Datos");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(500, 400);
        ventana.setLayout(new FlowLayout());

        JButton btnCrearBaseDatos = new JButton("Crear Base de Datos");
        JButton btnAgregarTablas = new JButton("Agregar Tablas a Base de Datos");
        JButton btnVerBasesDatos = new JButton("Ver Bases de Datos");
        JButton btnSalir = new JButton("Salir");

        ventana.add(btnCrearBaseDatos);
        ventana.add(btnAgregarTablas);
        ventana.add(btnVerBasesDatos);
        ventana.add(btnSalir);

        //Falta validación de crear una tabla a una base de datos existente
        btnCrearBaseDatos.addActionListener(e -> DatabaseManager.crearBaseDeDatos());
        btnAgregarTablas.addActionListener(e -> {
            String nombreBaseDatos = JOptionPane.showInputDialog("Ingresa el nombre de la base de datos a la que deseas agregar tablas:");
            if (nombreBaseDatos != null && !nombreBaseDatos.trim().isEmpty()) {
                DatabaseManager.agregarTablas(nombreBaseDatos);
            }
        });
        btnVerBasesDatos.addActionListener(e -> ListarBasesDeDatos.mostrarBasesDeDatos());
        btnSalir.addActionListener(e -> System.exit(0));

        ventana.setVisible(true);
    }
}
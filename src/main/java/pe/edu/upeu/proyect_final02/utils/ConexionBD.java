package pe.edu.upeu.proyect_final02.utils;

import java.io.File;
import java.sql.*;

public class ConexionBD {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String userHome = System.getProperty("user.home");
                File directories = new File(userHome + "/SistemaBibiotecaUPeU");
                if (!directories.exists()) {
                    directories.mkdirs();
                }
                //la db se guardara C:\Users\SistemaHoteleroUPeU\reserva.db\
                String dbPath = directories.getAbsolutePath() + "/biblioteca.db";
                String url = "jdbc:sqlite:" + dbPath;
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
                initDatabase();
                System.out.println("conexion success");
            }
            return connection;

        } catch (Exception e) {
            throw new RuntimeException("Error al conectar a la DB" + e.getMessage());
        }
    }
    private static void initDatabase() {
        String sqlCliente = "CREATE TABLE IF NOT EXISTS cliente (" +
                "idcliente INTEGER, " +
                "nombre TEXT, apellidos TEXT, dni TEXT, email TEXT, " +
                "PRIMARY KEY(idcliente));";

        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                "idusuario INTEGER, " +
                "user TEXT, password TEXT, tipoUser TEXT, nombre TEXT, apellidos TEXT, dni TEXT, " +
                "PRIMARY KEY(idusuario));";

        String sqlLibros = "CREATE TABLE IF NOT EXISTS libros (" +
                "idlibros INTEGER, " +
                "numero TEXT NOT NULL UNIQUE, " +
                "titulo TEXT, volumen TEXT, autor TEXT, " +
                "estado TEXT DEFAULT 'Disponible', " +
                "PRIMARY KEY(idlibros AUTOINCREMENT));";

        String sqlReserva = "CREATE TABLE IF NOT EXISTS reserva (" +
                "idreserva INTEGER, " +
                "clienteId INTEGER NOT NULL, libroId INTEGER NOT NULL, " +
                "fecha_reserva DATE NOT NULL, fecha_ingreso DATE NOT NULL, fecha_devolver DATE NOT NULL, " +
                "estado TEXT DEFAULT 'Activa', total REAL NOT NULL, usuarioId INTEGER, " +
                "PRIMARY KEY(idreserva AUTOINCREMENT), " +
                "FOREIGN KEY(clienteId) REFERENCES cliente(idcliente), " +
                "FOREIGN KEY(usuarioId) REFERENCES usuario(idusuario), " +
                "FOREIGN KEY(libroId) REFERENCES libros(idlibros));";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCliente);
            stmt.execute(sqlUsuario);
            stmt.execute(sqlLibros);
            stmt.execute(sqlReserva);

            // Usuario admin por defecto si la tabla está vacía
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuario")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO usuario (user, password, tipoUser, nombre) " +
                            "VALUES ('admin', 'admin123', 'ADMIN', 'Administrador');");
                    System.out.println("Usuario inicial 'admin' creado.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al inicializar tablas: " + e.getMessage());
        }
    }
}


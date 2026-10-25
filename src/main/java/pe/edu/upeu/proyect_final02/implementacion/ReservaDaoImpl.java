package pe.edu.upeu.proyect_final02.implementacion;

import pe.edu.upeu.proyect_final02.interfaces.IReservaDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.modelo.Reserva;
import pe.edu.upeu.proyect_final02.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReservaDaoImpl implements IReservaDao {
    public static final String ACTIVA = "Activa";
    public static final String NO_DEVUELTO = "No devuelto";
    public static final String DEVUELTO = "Devuelto";

    // Consulta base: une reserva con cliente y libros
    private static final String SELECT_BASE = " SELECT r.*,c.nombre|| ' ' ||c.apellidos AS nombre_completo," +
            " c.dni,c.email,l.numero,l.titulo,l.volumen,l.autor," +
            " l.estado AS estado_libro, l.precio " +
            " FROM reserva r,cliente c,libros l " +
            " WHERE r.clienteId = c.idcliente " +
            " AND r.libroId = l.idlibros ";
    @Override
    public boolean insert(Reserva r) {
        boolean flag = false;
        String query = "INSERT INTO reserva (clienteId,libroId,fecha_reserva,fecha_ingreso," +
                "fecha_devolver,estado,total) VALUES(?,?,?,?,?,?,?)";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setInt(1, r.getCliente().getIdcliente());
            st.setInt(2, r.getLibro().getIdlibros());
            //El LocalDate .toString devuelve "yyyy-MM-DD"
            st.setString(3, r.getFecha_reserva().toString());
            st.setString(4, r.getFecha_ingreso().toString());
            st.setString(5, r.getFecha_devolver().toString());
            st.setString(6, r.getEstado());
            st.setDouble(7, r.getTotal());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al incertar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Reserva r) {
        boolean flag = false;
        String query = "UPDATE reserva SET fecha_ingreso=?,fecha_devolver=?,estado=?,total=? WHERE idreserva=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1, r.getFecha_ingreso().toString());
            st.setString(2, r.getFecha_devolver().toString());
            st.setString(3, r.getEstado());
            st.setDouble(4, r.getTotal());
            st.setInt(5, r.getIdreserva());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public Reserva rearchById(int id) {
        Reserva r = null;
        String query = SELECT_BASE + " AND r.idreserva=? ";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    r = mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar reserva por Id:" + e.getMessage());
        }
        return r;
    }

    @Override
    public boolean delete(Reserva r) {
        boolean flag = false;
        String query = "DELETE FROM reserva WHERE idreserva=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setInt(1, r.getIdreserva());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al Eliminar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public List<Reserva> rlistAll() {
        List<Reserva> lista = new ArrayList<>();
        String query = SELECT_BASE + " ORDER BY r.idreserva DESC ";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery();
        ) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar Reserva: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean updateStatus(Reserva r) {
        boolean flag = false;
        String query = " UPDATE reserva SET estado = ? WHERE idreserva=?;";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1, r.getEstado());
            st.setInt(2, r.getIdreserva());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar reserva: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean validateLibro(Reserva r) {
        boolean flag = false;
        // hay conflicto si el libro sigue sin devolverse, o si otra reserva activa se cruza con las fechas
        String query = " SELECT count(*) FROM reserva WHERE libroId=? " +
                " AND (estado = 'No devuelto' " +
                " OR (estado = 'Activa' AND ? < fecha_devolver AND ? > fecha_ingreso)) ";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setInt(1, r.getLibro().getIdlibros());
            st.setString(2, r.getFecha_ingreso().toString());
            st.setString(3, r.getFecha_devolver().toString());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    flag = rs.getInt(1) == 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar Libro: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public List<Reserva> rsearch(String texto) {
        List<Reserva> lista = new ArrayList<>();
        String query = SELECT_BASE +
                " AND (c.dni LIKE ? OR c.nombre LIKE ? OR c.apellidos LIKE ? OR l.titulo LIKE ?) " +
                " ORDER BY r.idreserva DESC ";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            String patron = "%" + texto + "%";
            for (int i = 1; i <= 4; i++) {
                st.setString(i, patron);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar Reserva: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public int updateVencidas() {
        int cambiadas = -1;
        // las fechas se guardan como "yyyy-MM-dd", asi que se comparan como texto
        String query = "UPDATE reserva SET estado=? WHERE estado=? AND fecha_devolver < ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, NO_DEVUELTO);
            st.setString(2, ACTIVA);
            st.setString(3, LocalDate.now().toString());
            cambiadas = st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar reservas vencidas: " + e.getMessage());
        }
        return cambiadas;
    }
    // arma la reserva con sus objetos relacionados (cliente y libro)
    private Reserva mapear(ResultSet rs) throws SQLException {
        //instancia de objetos relacionados
        Cliente cl = new Cliente();
        cl.setIdcliente(rs.getInt("clienteId"));
        cl.setNombre(rs.getString("nombre_completo"));
        cl.setDni(rs.getString("dni"));
        cl.setEmail(rs.getString("email"));

        Libros l = new Libros();
        l.setIdlibros(rs.getInt("libroId"));
        l.setNumero(rs.getString("numero"));
        l.setTitulo(rs.getString("titulo"));
        l.setVolumen(rs.getString("volumen"));
        l.setAutor(rs.getString("autor"));
        l.setPrecio(rs.getDouble("precio"));
        l.setEstado(rs.getString("estado_libro"));
        //crear la reserva y asignar objetos
        Reserva r = new Reserva();
        r.setIdreserva(rs.getInt("idreserva"));
        r.setFecha_reserva(parseFecha(rs.getString("fecha_reserva")));
        r.setFecha_ingreso(parseFecha(rs.getString("fecha_ingreso")));
        r.setFecha_devolver(parseFecha(rs.getString("fecha_devolver")));
        r.setTotal(rs.getDouble("total"));
        r.setEstado(rs.getString("estado"));
        //conectamos los objetos
        r.setCliente(cl);
        r.setLibro(l);
        return r;
    }

    private LocalDate parseFecha(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha con formato invalido en la BD: " + texto);
            return null;
        }
    }
}

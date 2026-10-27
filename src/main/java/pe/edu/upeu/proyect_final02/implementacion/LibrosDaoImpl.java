package pe.edu.upeu.proyect_final02.implementacion;

import pe.edu.upeu.proyect_final02.interfaces.ILibrosDao;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibrosDaoImpl implements ILibrosDao {

    @Override
    public boolean insert(Libros l) {
        boolean flag = false;
        String query = "INSERT INTO libros(numero,titulo,volumen,autor,precio,estado)VALUES(?,?,?,?,?,?);";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1, l.getNumero());
            st.setString(2, l.getTitulo());
            st.setString(3, l.getVolumen());
            st.setString(4, l.getAutor());
            st.setDouble(5, l.getPrecio());
            st.setString(6, l.getEstado());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al incertar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Libros c) {
        boolean flag = false;
        String query = "UPDATE libros SET numero=?,titulo=?,volumen=?,autor=?,precio=?,estado=? WHERE idlibros=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1, c.getNumero());
            st.setString(2, c.getTitulo());
            st.setString(3, c.getAutor());
            st.setString(4, c.getVolumen());
            st.setDouble(5, c.getPrecio());
            st.setString(6, c.getEstado());
            st.setInt(7, c.getIdlibros());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public Libros searchById(int id) {
        Libros l = null;
        String query = "SELECT * FROM libros WHERE idlibros=?;";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    l = new Libros();
                    l.setIdlibros(rs.getInt("idlibros"));
                    l.setNumero(rs.getString("numero"));
                    l.setTitulo(rs.getString("titulo"));
                    l.setVolumen(rs.getString("volumen"));
                    l.setAutor(rs.getString("autor"));
                    l.setPrecio(rs.getDouble("precio"));
                    l.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al Buscar Libro por Id:" + e.getMessage());
        }
        return l;
    }

    @Override
    public boolean delete(Libros c) {
        boolean flag = false;
        String query = "DELETE FROM libros WHERE idlibros=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setInt(1, c.getIdlibros());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al Eliminar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public List<Libros> listAll() {

        List<Libros> lista = new ArrayList<>();
        String query = "SELECT * FROM libros;";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery();
        ) {
            while (rs.next()) {
                Libros l = new Libros();
                l.setIdlibros(rs.getInt("idlibros"));
                l.setNumero(rs.getString("numero"));
                l.setTitulo(rs.getString("titulo"));
                l.setVolumen(rs.getString("volumen"));
                l.setAutor(rs.getString("autor"));
                l.setPrecio(rs.getDouble("precio"));
                l.setEstado(rs.getString("estado"));
                lista.add(l);

            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Libros searchByTitulo(String titulo) {
        Libros l = null;
        String query = " SELECT * FROM libros WHERE titulo LIKE ? ";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, "%"+ titulo + "%");
            try( ResultSet rs = st.executeQuery()) {
                if (rs.next()){
                    l = new Libros();
                    l.setIdlibros(rs.getInt("Idlibros"));
                    l.setNumero(rs.getString("numero"));
                    l.setTitulo(rs.getString("titulo"));
                    l.setVolumen(rs.getString("volumen"));
                    l.setAutor(rs.getString("autor"));
                    l.setPrecio(rs.getDouble("precio"));
                    l.setEstado(rs.getString("estado"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar libro por titulo:" + e.getMessage());
        }
        return l;
    }

    @Override
    public boolean existsNumero(String numero, int idlibros) {
        String query = "SELECT count(*) FROM libros WHERE numero = ? AND idlibros <> ?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1, numero);
            st.setInt(2, idlibros);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al validar numero: " + e.getMessage());
        }
        return false;
    }
}

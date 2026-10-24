package pe.edu.upeu.proyect_final02.implementacion;

import pe.edu.upeu.proyect_final02.interfaces.ILibrosDao;
import pe.edu.upeu.proyect_final02.modelo.Libros;
import pe.edu.upeu.proyect_final02.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class LibrosDaoImpl implements ILibrosDao {

    @Override
    public boolean insert(Libros l) {
        boolean flag = false;
        String query = "INSERT INTO libros(numero,titulo,volumen,autor,precio,estado)VALUES(?,?,?,?,?,?);";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1,l.getNumero());
            st.setString(2,l.getTitulo());
            st.setInt(3,l.getIdlibros());
            st.setString(4,l.getAutor());
            st.setString(5,l.getVolumen());
            st.setDouble(6,l.getPrecio());
            st.setString(7,l.getEstado());
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
        String query = "UPDATE  SET libros numero=?,titulo=?, volumen=?,autor=?,precio=?,estado=?" + "WHERE idlibros";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1,c.getNumero());
            st.setString(2,c.getTitulo());
            st.setInt(3,c.getIdlibros());
            st.setString(3,c.getAutor());
            st.setString(4,c.getVolumen());
            st.setDouble(5,c.getPrecio());
            st.setString(6,c.getEstado());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public Libros searchById(int id) {
        return null;
    }

    @Override
    public boolean delete(Libros c) {
        return false;
    }

    @Override
    public List<Libros> listAll() {
        return List.of();
    }

    @Override
    public Libros searchByTitulo(String titulo) {
        return null;
    }
}

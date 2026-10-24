package pe.edu.upeu.proyect_final02.implementacion;

import pe.edu.upeu.proyect_final02.interfaces.IClienteDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;
import pe.edu.upeu.proyect_final02.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoImpl implements IClienteDao {

    @Override
    public boolean insert(Cliente c) {
        boolean flag = false;
        String query = "INSERT INTO cliente(nombre,apellidos,dni,email)VALUES(?,?,?,?);";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1, c.getNombre());
            st.setString(2, c.getApellidos());
            st.setString(3, c.getDni());
            st.setString(4, c.getEmail());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al incertar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public boolean update(Cliente c) {
        boolean flag = false;
        String query = "UPDATE cliente SET nombre=?,apellidos=?,dni=?,email=? WHERE idcliente=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setString(1, c.getNombre());
            st.setString(2, c.getApellidos());
            st.setString(3, c.getDni());
            st.setString(4, c.getEmail());
            st.setInt(5,c.getIdcliente());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public Cliente searchById(int id) {
        Cliente cl = null;
        String query = "SELECT * FROM cliente WHERE idcliente=?;";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1,id);
            try( ResultSet rs = st.executeQuery()) {
                if (rs.next()){
                    cl = new Cliente();
                    cl.setIdcliente(rs.getInt("idcliente"));
                    cl.setNombre(rs.getString("nombre"));
                    cl.setApellidos(rs.getString("apellidos"));
                    cl.setDni(rs.getString("dni"));
                    cl.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente por Id:" + e.getMessage());
        }
        return cl;
    }

    @Override
    public boolean delete(Cliente c) {
        boolean flag = false;
        String query = "DELETE FROM cliente WHERE idcliente=?";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);) {
            st.setInt(1, c.getIdcliente());
            st.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            System.out.println("Error al Eliminar: " + e.getMessage());
        }
        return flag;
    }

    @Override
    public List<Cliente> listAll() {
        List<Cliente> lista = new ArrayList<>();
        String query = "SELECT * FROM cliente;";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery();
        ) {
            while (rs.next()) {
                Cliente cl = new Cliente();
                cl.setIdcliente(rs.getInt("idcliente"));
                cl.setNombre(rs.getString("nombre"));
                cl.setApellidos(rs.getString("apellidos"));
                cl.setDni(rs.getString("dni"));
                cl.setEmail(rs.getString("email"));
                lista.add(cl);

            }

        } catch (SQLException e) {
            System.out.println("Error al listar Cliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Cliente searchByDni(String dni) {
        Cliente cl = null;
        String query = "SELECT * FROM cliente WHERE dni=?;";
        try (Connection cn = ConexionBD.getConnection();
             PreparedStatement st = cn.prepareStatement(query)) {
            st.setString(1,dni);
            try( ResultSet rs = st.executeQuery()) {
                if (rs.next()){
                    cl = new Cliente();
                    cl.setIdcliente(rs.getInt("idcliente"));
                    cl.setNombre(rs.getString("nombre"));
                    cl.setApellidos(rs.getString("apellidos"));
                    cl.setDni(rs.getString("dni"));
                    cl.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por dni:" + e.getMessage());
        }
        return cl;
    }
}

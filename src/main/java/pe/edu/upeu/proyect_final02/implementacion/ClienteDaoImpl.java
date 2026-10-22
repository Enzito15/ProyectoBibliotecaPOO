package pe.edu.upeu.proyect_final02.implementacion;

import pe.edu.upeu.proyect_final02.interfaces.IClienteDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;

import java.util.List;

public class ClienteDaoImpl implements IClienteDao {

    @Override
    public boolean insert(Cliente c) {
        return false;
    }

    @Override
    public boolean update(Cliente c) {
        return false;
    }

    @Override
    public Cliente searchById(int id) {
        return null;
    }

    @Override
    public boolean delete(Cliente c) {
        return false;
    }

    @Override
    public List<Cliente> listAll() {
        return List.of();
    }

    @Override
    public Cliente searchByDni(String dni) {
        return null;
    }
}

package pe.edu.upeu.proyect_final02.interfaces;

import pe.edu.upeu.proyect_final02.modelo.Cliente;

import java.util.List;

public interface IClienteDao {
    public boolean insert(Cliente c);
    public boolean update(Cliente c);
    public Cliente searchById(int id);
    public boolean delete(Cliente c);
    public List<Cliente> listAll();

    public Cliente searchByDni(String dni);
}

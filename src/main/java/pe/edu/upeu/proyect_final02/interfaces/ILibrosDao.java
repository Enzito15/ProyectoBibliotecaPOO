package pe.edu.upeu.proyect_final02.interfaces;

import pe.edu.upeu.proyect_final02.modelo.Libros;

import java.util.List;

public interface ILibrosDao {
    public boolean insert(Libros l);
    public boolean update(Libros c);
    public Libros searchById(int id);
    public boolean delete(Libros c);
    public List<Libros> listAll();

    public Libros searchByTitulo(String titulo);
}

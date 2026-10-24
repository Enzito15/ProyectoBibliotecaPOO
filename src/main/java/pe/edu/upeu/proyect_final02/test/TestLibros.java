package pe.edu.upeu.proyect_final02.test;

import pe.edu.upeu.proyect_final02.implementacion.LibrosDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.ILibrosDao;
import pe.edu.upeu.proyect_final02.modelo.Libros;

import java.util.List;

public class TestLibros {
    static ILibrosDao dao = new LibrosDaoImpl();
    public static void listar() {
        List<Libros> lista = dao.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Libros libros : lista) {
                System.out.println("Id_Libros: " + libros.getIdlibros());
                System.out.println("Numero: " + libros.getNumero());
                System.out.println("Titulo: " + libros.getTitulo());
                System.out.println("Volumen: " + libros.getVolumen());
                System.out.println("Autor: " + libros.getAutor());
                System.out.println("Precio: " + libros.getPrecio());
                System.out.println("Estado: " + libros.getEstado());

            }
        } else {
            System.out.println("No hay registros");

        }
    }
    public static void incertar() {
        Libros l = new Libros();
        l.setNumero("1");
        l.setTitulo("Harry Potter y la pieda filosofal");
        l.setVolumen("1");
        l.setAutor("J.K");
        l.setPrecio(10.0);
        l.setEstado("Disponible");

        boolean result = dao.insert(l);
        if (result) {
            System.out.println("incert correcto");
        } else {
            System.out.println("incert incorrecto");
        }
    }
    public static void eliminar() {
        Libros l = new Libros();
        l.setIdlibros(1);
        boolean result = dao.delete(l);
        if (result) {
            System.out.println("delete succes");
        } else {
            System.out.println("delete error");
        }
    }

    public static void main(String[] args) {
        System.out.println("*************************");
        incertar();
        System.out.println("*************************");
        listar();
    }
}

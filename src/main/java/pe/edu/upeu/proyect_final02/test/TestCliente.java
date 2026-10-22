package pe.edu.upeu.proyect_final02.test;

import pe.edu.upeu.proyect_final02.implementacion.ClienteDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.IClienteDao;
import pe.edu.upeu.proyect_final02.modelo.Cliente;

import java.util.List;

public class TestCliente {
    static IClienteDao cl = new ClienteDaoImpl();

    public static void listar() {
        List<Cliente> lista = cl.listAll();
        if (lista != null && !lista.isEmpty()) {
            for (Cliente cliente : lista) {
                System.out.println("Id_Cliente: " + cliente.getIdcliente());
                System.out.println("NOMBRE: " + cliente.getNombre());
                System.out.println("APELLLIDO: " + cliente.getApellidos());
                System.out.println("DNI: " + cliente.getDni());
                System.out.println("EMAIL: " + cliente.getEmail());
            }
        } else {
            System.out.println("No hay registros");

        }
    }

    public static void eliminar() {
        Cliente c = new Cliente();
        c.setIdcliente(1);
        boolean result = cl.delete(c);
        if (result) {
            System.out.println("delete succes");
        } else {
            System.out.println("delete error");
        }
    }
    public static void incertar() {
        Cliente c = new Cliente();
        c.setNombre("Enzo Yair");
        c.setApellidos("D M");
        c.setDni("61407619");
        c.setEmail("enzocrak2345@gmail.com");
        boolean result = cl.insert(c);
        if (result) {
            System.out.println("incert correcto");
        } else {
            System.out.println("incert incorrecto");
        }
    }


    public static void main(String[] args) {
        System.out.println("\n****************************");
        listar();
    }
}

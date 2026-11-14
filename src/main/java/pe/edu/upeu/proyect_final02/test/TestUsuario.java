package pe.edu.upeu.proyect_final02.test;

import pe.edu.upeu.proyect_final02.implementacion.UsuarioDaoImpl;
import pe.edu.upeu.proyect_final02.interfaces.IUsuarioDao;
import pe.edu.upeu.proyect_final02.modelo.Usuario;

public class TestUsuario {
    private static IUsuarioDao dao =  new UsuarioDaoImpl();
    public void valid_Login(){

        Usuario us = dao.login("admin", "admin");
        if (us !=null){
            System.out.println("ID:USUARIO: "+us.getIdusuario());
            System.out.println("NOMBRE: "+us.getNombre());
            System.out.println("APELLIDOS: "+us.getApellidos());
            System.out.println("DNI: " +us.getDni());
            System.out.println("TIPO_USER: " + us.getTipoUser());
        }else{
            System.out.println("Error de login");
        }
    }

    public static void main(String[] args) {
        TestUsuario t = new TestUsuario();
        t.valid_Login();
    }
}

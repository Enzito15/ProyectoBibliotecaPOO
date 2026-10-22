package pe.edu.upeu.proyect_final02.test;

import pe.edu.upeu.proyect_final02.utils.ConexionBD;

import java.sql.Connection;

public class TestConexion {
    public static void testConexion(){
        try {
            Connection connection = ConexionBD.getConnection();
            if (connection !=null && connection.isClosed()){
                System.out.println("conexion correcta");
            }else{
                System.out.println("error de conexion");
            }

        }catch (Exception e){
            System.out.println("error de conexion"+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testConexion();
    }
}

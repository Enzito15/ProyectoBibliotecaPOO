package pe.edu.upeu.proyect_final02.interfaces;

import pe.edu.upeu.proyect_final02.modelo.Reserva;

import java.util.List;

public interface IReservaDao {
    public boolean insert(Reserva r);
    public boolean update(Reserva r);
    public Reserva rearchById(int id);
    public boolean delete(Reserva r);
    public List<Reserva> rlistAll();
    public boolean updateStatus(Reserva r);
    public boolean validateLibro(Reserva r);
    public List<Reserva> rsearch(String texto);
    public int updateVencidas();
}

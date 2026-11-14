package pe.edu.upeu.proyect_final02.modelo;

public class UserSession {
    private static UserSession instance;
    private Usuario UserLogin;

    private UserSession(){

    }
    public static UserSession getInstance(){
        if (instance == null){
            instance = new UserSession();
        }
        return instance;
    }
    public Usuario getUserLogin(){
        return UserLogin;
    }
    public void setUserLogin(Usuario usuario){
        this.UserLogin = usuario;
    }
    public void cleanSession(){
        UserLogin = null;
    }
}

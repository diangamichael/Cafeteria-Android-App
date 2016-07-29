package skallaje.cafeteria_app.cs442.com;

/**
 * Created by shara on 11/2/2015.
 */
public class User {

    String ID;
    String password;
    String admin;
    String emailID;
    int sent;
    long phone;
    String location;

    public User(String id, String pwd, String adm, String email, int e_sent, long ph, String def_loc){
        ID = id;
        password=pwd;
        admin=adm;
        emailID=email;
        sent=e_sent;
        phone=ph;
        location=def_loc;
    }
}

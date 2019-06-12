import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
    private String data;
    private String pas;
    private String log;
    public DataConnection(String data, String log, String pas){
        this.data = data;
        this.log= log;
        this.pas = pas;

    }
    public Connection connect(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = ("jdbc:postgresql://localhost:5432/" + data);
            //String log = "postgres";
            //String pas = "postgres";
            Connection con = DriverManager.getConnection(url, log, pas);
            return con;
        }
        catch(ClassNotFoundException | SQLException ex){
            System.out.println("Connection error, plz try again...");
            return null;
        }
    }
}

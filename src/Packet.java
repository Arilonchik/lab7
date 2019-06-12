import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

class Packet implements Serializable {
    private String password, command, login, argument, path;
    private boolean at;
    private CopyOnWriteArrayList<Shelter> collection;
    private String ans;

    public Packet(String command, String argument, String login, String password) {
        this.command = command;
        this.argument = argument;
        this.login = login;
        this.password = password;
        at=false;
    }

    public Packet(String command, String login, String password) {
        this.command = command;
        this.login = login;
        this.password = password;
        at = false;
    }

    public Packet (String command, String login, String pass, boolean at){
        this.command = command;
        this.login = login;
        this.password = pass;
        this.at = at;
    }

    public Packet (CopyOnWriteArrayList<Shelter> collection) {
        this.collection = collection;
    }

    public Packet(String ans, boolean at){
        this.at = at;
        this.ans = ans;
        collection = null;
    }


    public String getAns(){return this.ans;}

    public String getCommand() {
        return command;
    }

    public void setPath(String path){
        this.path=path;
    }

    public boolean getAt(){
        return at;
    }

    public String getLogin() {
        return login;
    }

    public String getArgument() {
        return argument;
    }

    public String getPassword() {
        return password;
    }

    public CopyOnWriteArrayList<Shelter> getCollection() { return collection; }
}

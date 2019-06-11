import java.net.InetAddress;

public class User {
    private InetAddress IPadress;
    private int port;
    public User(InetAddress i, int p){
        this.IPadress = i;
        this.port = p;
    }
    public int getPort(){return port;}
    public InetAddress getIPadress() {return IPadress;}

    @Override
    public boolean equals(Object ob) {
        if(this==ob) {
            return true;
        }

        if(ob instanceof User) {
            User rO = (User) ob;
            return (this.IPadress.equals(rO.getIPadress())) && (this.port == rO.getPort());
        }
        return false;
    }
}

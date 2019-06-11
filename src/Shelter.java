import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Shelter extends RealObject implements Comparable<Shelter>, Serializable {
    //private Date date;
    private String zone;
    private String creator;

    Shelter(double x, String name, String log) {
        super(x, name);
        //date = new Date();
        zone = getZone();
        this.creator = log;
    }

    Shelter(double x, String name, String log, String date) {
        super(x, name);
        //date = new Date();
        zone = date;
        this.creator = log;
    }

    Shelter() {
        super();
        //date = new Date();
        zone = getZone();
    }

    //public String getDate() { return date.toString(); }

    public String getZone() {
        ZonedDateTime zone = ZonedDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy");
        String str = zone.format(format) + " " +zone.getZone() + " " + zone.getOffset();
        return str;
    }

    public String getName(){
        return this.name;
    }

    public double getPos(){
        return this.x;
    }

    public String getDate(){
        return this.zone;
    }

    public String getCreator(){return this.creator;}

    public void setCreator(String creator) {this.creator = creator;}

    @Override
    public String toString() {
        return "Object: " + getClass().getName()
                + ", Name: " + name
                + ", Position: " + x
                + ", ZonedDate: " + zone
                + ", Creator: " + creator
                + "\n";
    }

    @Override
    public boolean equals(Object ob) {
        if(this==ob) {
            return true;
        }

        if(ob instanceof Shelter) {
            Shelter rO = (Shelter) ob;
            return (this.name.equals(rO.getMyName())) && (this.x == rO.getX() && (this.creator.equals(rO.getCreator())));
        }
        return false;
    }

    @Override
    public int compareTo(Shelter shelter) {
        return name.compareTo(shelter.getMyName());
    }
}

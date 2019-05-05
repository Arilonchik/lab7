import java.util.Date;

public class Shelter extends RealObject implements Comparable<Shelter>{
    private Date date;
    private double height;

    Shelter(double x, String name) {
        super(x, name);
        date = new Date();
    }

    Shelter() {
        super();
        date = new Date();
    }

    public String getDate() {
        return date.toString();
    }


    @Override
    public String toString() {
        return "Object: " + getClass().getName() + ", Name: " + name + ", Position: " + x + "\n";
    }

    @Override
    public boolean equals(Object ob) {
        if(this==ob) {
            return true;
        }

        if(ob instanceof Shelter) {
            Shelter rO = (Shelter) ob;
            return (this.name.equals(rO.getMyName())) && (this.x == rO.getX());
        }
        return false;
    }

    @Override
    public int compareTo(Shelter shelter) {
        return name.compareTo(shelter.getMyName());
    }
}

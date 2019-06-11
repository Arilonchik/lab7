import java.io.Serializable;

public class RealObject extends AbstractObject implements Map, Sound, Serializable {
    double x;
    double hearVolume = 0;
    double speakVolume = 0;
    boolean isXSetted=true;
    RealObject() {
        name = "Unknown Object";
        x = 0;
    }

    RealObject(String name) {
        this.name = name;
    }

    RealObject(double x) {
        this.x = x;
        isXSetted=false;
        name = "Unknown Object";
    }

    RealObject(double x, String name) {
        this.x = x;
        isXSetted=false;
        this.name = name;
    }

    void setX(double x) {
        this.x = x;
        isXSetted=false;
    }

    void setName(String name) { this.name = name; }

    @Override
    public double getX() { return x; }

    @Override
    public String getMyName() { return name; }

    @Override
    public double getSpeakedVol() { return speakVolume; }

    @Override
    public double getHeardVol() { return hearVolume; }

    @Override
    public void setSpeakedVol(double vol) { speakVolume = vol; }

    @Override
    public void setHeardVol(RealObject obj) { hearVolume = obj.getSpeakedVol()/Math.abs(x-obj.x); }

    @Override
    public boolean equals(Object ob) {
        if(this==ob) {
            return true;
        }
        if(ob instanceof RealObject) {
            RealObject rO = (RealObject)ob;
            if((this.name.equals(rO.getMyName()))&&(this.x == rO.getX())&&(this.hearVolume == rO.getHeardVol())&&(this.speakVolume == rO.getSpeakedVol())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int cons = 28;
        int res = 1;
        res = cons*res + name.length()*(cons-2);
        res += (int)x*(cons+14);
        return res;
    }

    @Override
    public String toString() {
        return "Объект класса "+getClass().getName()+", имя:"+name+", координата:"+x;
    }
}

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Human extends Creature {
    private String sex;
    final double SPEED = 1;
    final double TYPE_CREATURE_POWER_KOEF=1;

    Human() {
        super();
        name="Unknown Human";
        phys*=TYPE_CREATURE_POWER_KOEF;
    }

    Human (double x, String name, String sex, PhysicalStatus p) {
        super(x, name, p);
        this.sex = sex;
        phys*=TYPE_CREATURE_POWER_KOEF;
    }

    void myEnergy() {
        class Local {
            void show() {
                System.out.println(name + "'s energy is " + energy);
            }
        }
        Local local = new Local();
        local.show();
    }

    Shelter shelterNearME(CopyOnWriteArrayList<? extends Shelter> ar) {
        Shelter ob = null;
        int min = (int) Math.abs(ar.get(0).getX() - getX());
        for (Shelter shelter : ar) {
            if (Math.abs(shelter.getX() - getX()) <= min){
                min = (int) Math.abs(shelter.getX() - getX());
                ob = shelter;
            }
        }
        return ob;
    }

    @Override
    public int hashCode() {
        int res;
        res=super.hashCode();
        res+=(int)(sex.length()*19/2);
        return res;
    }

    @Override
    double getShag() {
        double shag=phys*SPEED*TYPE_CREATURE_POWER_KOEF;
        double energyShag;
        if(energy > 0) {
            energy -= RUN_ENERGY_KOEF/(phys*TYPE_CREATURE_POWER_KOEF);
            energyShag=shag;
        } else {
            energy=0;
            energyShag=shag/2;
        }
        return energyShag;
    }


    @Override
    public boolean equals(Object ob) {
        if(this==ob) {
            return true;
        }
        if(ob instanceof Human) {
            Human hu = (Human)ob;
            if(super.equals(hu)&&(this.sex.equals(hu.sex))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Объект класса "+getClass().getName()+", имя:"+name +", пол:"+ sex +", координата:"+x+", энергия:"+energy;
    }
}

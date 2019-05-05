public class Creature extends RealObject {
    private PhysicalStatus stat;
    double phys;
    double energy = 100d;
    double RUN_ENERGY_KOEF = 50;
    private int direction;

    Creature() {
        super();
        name = "Unknown Creature";
    }

    Creature(double x, String name, PhysicalStatus p) {
        super(x, name);
        stat = p;
        phys = stat.getKoeff();
    }

    void setStatus(PhysicalStatus p) {
        stat = p;
        phys = stat.getKoeff();
    }

    void setEnergy(double newEnergy) {
        energy = newEnergy;
    }

    PhysicalStatus getStatus() {
        return stat;
    }

    double getEnergy() {
        return energy;
    }


    void simpleRun(double shag) {
        x+=shag;
        System.out.println(name + " is running. X: " + x);
    }

    double getShag() {
        double shag=phys;
        double energyShag;
        if(energy > 0) {
            energy -= RUN_ENERGY_KOEF/(phys);
            energyShag=shag;
        } else {
            energy=0;
            energyShag=shag/2;
        }
        return energyShag;
    }

    void run(int direction) {
        simpleRun(getShag()*direction);
    }

    void run(int direction, double xOb) {
        double energyShag = getShag()*direction;
        if(Math.abs(x-xOb) <= Math.abs(energyShag)) {
            x = xOb;
        } else {
            simpleRun(energyShag);
        }
    }

    void runTo(RealObject rOb) {
        try {
            if((this.getX()==0)&&(this.isXSetted))
                throw new XException(this);
            if((rOb.getX()==0)&&(rOb.isXSetted))
                throw new XException(rOb);
        } catch(XException e1) { System.out.println(e1); }


        System.out.println(name + " is running to "+ rOb.getMyName());
        while(this.getX()!=rOb.getX()){
            if(this.getX()>rOb.getX()) {
                direction=-1;
            }
            if(this.getX()<rOb.getX()) {
                direction=1;
            }
            run(direction,rOb.getX());
        }
        System.out.println(name + " has runned to "+ rOb.getMyName());
    }


    void runFrom(RealObject rOb) {
        try {
            if((this.getX()==0)&&(this.isXSetted))
                throw new XException(this);
            if((rOb.getX()==0)&&(rOb.isXSetted))
                throw new XException(rOb);
        } catch(XException e) { System.out.println(e); }

        System.out.println(name + " is running from "+ rOb.getMyName());
        this.setHeardVol(rOb);
        System.out.println(rOb.getMyName()+"'s volume:"+hearVolume);

        while (hearVolume > 2) {
            this.setHeardVol(rOb);

            if((this.getX()-rOb.getX())>0) {
                int direction=1;
                this.run(direction);
            }

            if((this.getX()-rOb.getX())<0) {
                int direction=-1;
                this.run(direction);
            }
            System.out.println(rOb.getMyName()+"'s volume:"+hearVolume);
        }
        System.out.println(name + " has runned from "+ rOb.getMyName());

    }

    @Override
    public int hashCode() {
        int res;
        res=super.hashCode();
        res+=(int)(phys*12 + energy*13);
        return res;
    }

    @Override
    public boolean equals(Object ob) {
        if(this==ob) {
            return true;
        }

        if(ob instanceof Creature) {
            Creature cr = (Creature)ob;
            if(super.equals(cr)&&(this.stat==cr.stat)&&(this.phys==cr.phys)&&(this.energy==cr.energy)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Объект класса "+getClass().getName()+", имя:"+name+", координата:"+x+", энергия:"+energy;
    }
}

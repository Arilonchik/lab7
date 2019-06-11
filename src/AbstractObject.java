import java.io.Serializable;

public abstract class AbstractObject implements Serializable {
    String name;
    public String getMyName() {
        return name;
    }
}

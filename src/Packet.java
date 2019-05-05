import java.io.Serializable;

public class Packet implements Serializable {
    String command;
    Shelter shelter;

    Packet (String command) {
        this.command = command;
    }

    Packet (String command, Shelter shelter) {
        this.command = command;
        this.shelter = shelter;

    }
}

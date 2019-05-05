import java.io.IOException;
import java.net.DatagramSocket;

public class UDPConnection {
    private final DatagramSocket socket;

    public UDPConnection(DatagramSocket socket) throws IOException {
        this.socket = socket;
    }



    @Override
    public String toString() {
        return "UDPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}

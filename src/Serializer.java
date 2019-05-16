import java.io.*;

public class Serializer {
    static byte[] serialize(Packet packet) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
        objectOutputStream.writeObject(packet);
        objectOutputStream.flush();

        return byteStream.toByteArray();
    }

    static Packet deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        return (Packet)objectInputStream.readObject();
    }
}

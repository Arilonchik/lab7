public class XException extends Exception {
    RealObject o;
    XException(RealObject ob) {
        o = ob;
    }

    @Override
    public String toString() {
        o.setX(0);
        return "Объекту '"+o.toString()+"' не была задана координата X. Теперь она устанавливается = 0.\n";
    }
}

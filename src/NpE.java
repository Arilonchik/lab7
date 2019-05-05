public class NpE extends RuntimeException {
    String name;

    @Override
    public String toString()
    {
        return "Ссылке не присвоен какой-нибудь объект.";
    }
}

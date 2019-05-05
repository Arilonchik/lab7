
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;

public class AutReg {

    //Первая авторизация
    public String auth(Connection c, String log, String pas){
        try {

            Statement stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM \"USERS\" WHERE \"EMAIL\"='" +log+"';");
            System.out.println("keksus" + log);
            if(rs.next()){
            System.out.println(rs.getString(1));
            String p = rs.getString(2);
            if (p.equals(pas)){
                stmt.close();
                return "Success";
            }
            else{
                stmt.close();
                return "Wrong pas";
            }
            } else{
                stmt.close();
                return "Unregestred email";
            }
        }catch( SQLException e){
            e.printStackTrace();
            return "Something wrong...";
        }
    }
    //Регестрация
    public String reg(Connection c, String em) {
        try {

            //Сюда добавить генерацию пароля и отправление его по почте javamail

            if(em.matches("\\w*[@]\\w*[.]\\w*")){
            String pas = generatePassword();
                try {
                    pas = encryptMD2(pas);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM \"USERS\" WHERE \"EMAIL\"='" +em+"';");
            System.out.println("keksus" + em);
            if(!rs.next()) {
                String sql ="INSERT INTO \"USERS\"(\"EMAIL\",\"PASS\") VALUES (?,?);";
                PreparedStatement psmt = c.prepareStatement(sql);
                psmt.setString(1,"'" +em+"'");
                psmt.setString(2,"'" +pas+"'");
                psmt.executeUpdate();
                psmt.close();
                stmt.close();
                return "Success";
            }
            stmt.close();}
            else{
                return "Invalid email";
            }
        }catch( SQLException e){
            System.out.println("ORDA SASATB");
            e.printStackTrace();
            return "Something wrong";
        }
        return "This email already registered";

    }

    static String generatePassword() {
        String password = new Random().ints(48, 123)
                .filter(i -> (i <= 57 || (i >= 65 && i <= 90) || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return password;


    }

    static String encryptMD2(String string) throws NoSuchAlgorithmException {
        MessageDigest md2 = MessageDigest.getInstance("MD2");
        byte[] bytes = md2.digest(string.getBytes());
        BigInteger no = new BigInteger(1, bytes);
        String hashtext = no.toString(16);

        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }
}

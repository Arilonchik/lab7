import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParseXML {
    String str;

    CopyOnWriteArrayList<Shelter> map = new CopyOnWriteArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Double> positions = new ArrayList<>();

    CopyOnWriteArrayList<Shelter> parse(String file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(input);
            String line = br.readLine().trim();
            while (line != null) {
                if (line.contains("<Name>")){
                    str = line.substring(line.indexOf(">")+1, line.lastIndexOf("<"));
                    names.add(str);
                }
                if (line.contains("<Position>")){
                    str = line.substring(line.indexOf(">")+1, line.lastIndexOf("<"));
                    positions.add(Double.parseDouble(str));
                }
                line = br.readLine();
            }
            fileInputStream.close();
            input.close();
            br.close();
        } catch (IOException e) {
            System.out.println("File not found.");
        } catch (NullPointerException e) {
             return map;
        }

        for (int i = 0; i < names.size(); i++) {
            map.add(new Shelter(positions.get(i), names.get(i)));
        }

        return map;
    }
}

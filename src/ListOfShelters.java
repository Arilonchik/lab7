import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for using collection
 * There are some methods for use collection of Shelter
 */
class ListOfShelters {
    CopyOnWriteArrayList<Shelter> sh;
    private Connection con;

    private Gson builder = new GsonBuilder()
            .registerTypeAdapter(Shelter.class, new CustomConverter())
            .create();

    private Date date;

    ListOfShelters(String fileName) {
        ParseXML xml = new ParseXML();
        sh = xml.parse(fileName);
        date = new Date();
    }

    ListOfShelters(CopyOnWriteArrayList<Shelter> sh, Connection con) {
        this.sh = sh;
        date = new Date();
        this.con = con;
    }

    /**
     * Add new item in collection
     * You enter command 'add' after that the next line is the name and position of item which you want to add.
     */
    String add(String object, String log) throws JsonSyntaxException {

            int size = sh.size();
            Shelter shelter = builder.fromJson(object, Shelter.class);
            if (shelter != null) {
                shelter.setCreator(log);
                sh.add(shelter);
            }

            if (size != sh.size()) { return "Shelter is successfully added."; }
            else { return "Enter is wrong."; }
    }

    /**
     * Show all Shelter in collection.
     */
    String show() {
        return sh.stream()
                .sorted(Shelter::compareTo)
                .map(Shelter::toString)
                .reduce((s1, s2) -> s1 + s2)
                .orElse("Collection is empty");
    }

    /**
     * Show some information about this collection.
     */
    String info() {
            if (sh.size() != 0) {
                return "Size of this collection: " + sh.size() +
                        "\nType of collection: " + sh.get(0).getClass().getName() +
                        "\nDate of initialization: " + date.toString();
            } else {
                return "Collection is empty";
            }

    }

    /**
     * Remove last item form collection.
     */
    String remove_last(String log) { // Correct
        Shelter last = sh.stream().findFirst().orElse(null);
        System.out.println(last.getCreator());
        System.out.println(log);
        if (last != null && last.getCreator().equals(log)) {
            sh.stream().sorted(Shelter::compareTo)
                    .skip(sh.size() -1)
                    .findAny()
                    .orElse(null);
            sh.remove(last);
            return "The element was deleted";
        } else {
            return "Collection is empty or u don't have permissions for this shelter";
        }
    }

    /**
     * Remove first item form collection.
     */
    String remove_first(String log) { // Correct
        Shelter first = sh.stream()
                .sorted(Shelter::compareTo)
                .findFirst()
                .orElse(null);

        if (first != null && first.getCreator().equals(log)) {
            sh.remove(first);
            return "The element was deleted";
        } else {
            return "Collection is empty or u don't have permission for this shelter";
        }
    }

    /**
     * Remove the item{name : position}
     */

    String remove(String object, String log) throws JsonSyntaxException, NullPointerException{
           Shelter remove_object = builder.fromJson(object, Shelter.class);

           int size = sh.size();
           remove_object.setCreator(log);
           if (sh.contains(remove_object) && remove_object.getCreator().equals(log)) {
               sh.remove(remove_object);
           } else {
               return "There isn't this shelter in collection or U don't have permissions";
           }

           if (size != sh.size()) {
               return "Shelter is deleted";
           } else {
               return "Shelter isn't deleted\nU don't have permissions";
           }
    }

    /**
     * Add item if it's the biggest Shelter in collection
     */

    String addIfMax(String object, String log) {
        Shelter compare_object = builder.fromJson(object, Shelter.class);
        compare_object.setCreator(log);
        Optional<Shelter> maxShelter = sh.stream().min(Shelter::compareTo);
        if (maxShelter.isPresent()) {
            if (compare_object.compareTo(maxShelter.get()) < 0) {
                sh.add(compare_object);
                return "Shelter is successfully added";
            } else if (maxShelter.get().compareTo(compare_object) == 0) {
                return "This shelter has already contained in collection.";
            } else {
                return "Shelter isn't the biggest element.";
            }
        } else {
            sh.add(compare_object);
            return "Shelter is successfully added";
        }
    }

    /**
     * If you enter command 'exit' all information about collection save in file "save_data.xml"
     */
    void save() {
        try{
            File file = new File("Downloads/collection.xml");
            writeInFile(file);
            savePost();
        } catch (IOException e) {
            System.out.println("File hasn't been saved.");
        }

        System.out.println("\nFile has been saved.");
    }

    String save (String filename) {
        File file = new File(filename.trim());
        savePost();
        try {
            writeInFile(file);
        } catch (IOException e) {
            return "File isn't saved.";
        }

        return "File is saved.";
    }

    public void savePost(){
        boolean check = true;
        try {
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("DROP TABLE IF EXISTS \"COLLECTION\";\n" +
                    "CREATE TABLE \"COLLECTION\"\n" +
                    "(\"NAME\" VARCHAR NOT NULL,\n" +
                    "\"DATE\" VARCHAR NOT NULL,\n" +
                    "\"CREATOR\" VARCHAR NOT NULL,\n" +
                    "\"POSITION\" VARCHAR NOT NULL\n" +
                    ");");
            stmt.executeUpdate();
            stmt.close();
            for (Shelter shel:sh) {
                boolean tp = insert(con,shel.getName(),shel.getDate(),shel.getCreator(),String.valueOf(shel.getPos()));
                if(tp == false){
                    check = false;
                }
            }
            if(check){
                // Завершаем транзакцию - подтверждаем
                con.commit();}
            else{
                // Вызов rollback отменит все внесенные изменения
                con.rollback();}
            // Возвращаем свойство AutoCommit в true
            con.setAutoCommit(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveOnServer() {
        try{
            File file = new File("Downloads/backup.xml");
            writeInFile(file);
        } catch (IOException e) {
            System.out.println("File hasn't been saved.");
        }
        savePost();

    }

    String load (String filename) {
        ParseXML p = new ParseXML();
        File file = new File(filename);
        if (file.exists()) {
            if (filename.endsWith(".xml")) {
                CopyOnWriteArrayList<Shelter> addCollection = p.parse(filename);
                if (sh.addAll(addCollection)) {
                    return "Collection is added.";
                } else return "Something is wrong.";
            } else return "This file isn't xml file.";
        } else {
            return "Please check name of file.";
        }
    }

    /**
     * Sort collection by name.
     */
    // Если отправить коллекцию в ArraySort то должна реализоваться сортировка по умполчанию через компаротор
    String sort() {
        sh = sh.stream()
                .sorted(Shelter::compareTo)
                .collect(CopyOnWriteArrayList::new, CopyOnWriteArrayList::add, CopyOnWriteArrayList::addAll);
        return "Sorted";
    }

    private void writeInFile(File file) throws IOException {
        if (!file.exists()) file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bfr = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        bfr.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
        bfr.write("<ShelterList>\n");
        for (Shelter shelter : sh) {
            bfr.newLine();
            bfr.write("\t<Shelter>\n");
            bfr.write("\t\t<Name>" + shelter.name + "</Name>\n");
            bfr.write("\t\t<Position>" + shelter.x + "</Position>\n");
            bfr.write("\t\t<Zone>" + shelter.getZone() + "</Zone>\n");
            bfr.write("\t\t<Creator>" + shelter.getCreator() + "</Creator>\n");
            bfr.write("\t</Shelter>\n");
        }
        bfr.write("</ShelterList>\n");
        bfr.close();
    }

    private boolean insert(Connection con, String name, String date, String creator, String pos) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"COLLECTION\"(\"NAME\",\"DATE\",\"CREATOR\",\"POSITION\") VALUES (?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, date);
            stmt.setString(3, creator);
            stmt.setString(4, pos);
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e){
            return false;
        }

    }
}

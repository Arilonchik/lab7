import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.sql.Connection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for using collection
 * There are some methods for use collection of Shelter
 */
class ListOfShelters {
    CopyOnWriteArrayList<Shelter> sh;

    private Gson builder = new GsonBuilder()
            .registerTypeAdapter(Shelter.class, new CustomConverter())
            .create();

    private Date date;

    ListOfShelters(String fileName) {
            ParseXML xml = new ParseXML();
            sh = xml.parse(fileName);
            date = new Date();
    }

    /**
     * Add new item in collection
     * You enter command 'add' after that the next line is the name and position of item which you want to add.
     */
    String add(String object, Connection c) throws JsonSyntaxException {
            int size = sh.size();
            Shelter shelter = builder.fromJson(object, Shelter.class);

            if (shelter != null) { sh.add(shelter); }

            if (size != sh.size()) { return "Shelter is successfully added."; }
            else { return "Enter is wrong."; }
    }

    /**
     * Show all Shelter in collection.
     */
    String show(Connection c) {
        return sh.stream()
                .sorted(Shelter::compareTo)
                .map(Shelter::toString)
                .reduce((s1, s2) -> s1 + s2)
                .orElse("Collection is empty");
    }

    /**
     * Show some information about this collection.
     */
    String info(Connection c) {
        if (sh.isEmpty()) return "Collection is empty";
        else {
            return "Size of this collection: " + sh.size() +
                    "\nType of collection: " + sh.get(0).getClass().getName() +
                    "\nDate of initialization: " + date.toString();
        }
    }

    /**
     * Remove last item form collection.
     */
    String remove_last(Connection c) { // Correct
        Shelter last = sh.stream().findFirst().orElse(null);
        if (last != null) {
            sh.stream().sorted(Shelter::compareTo)
                    .skip(sh.size() -1)
                    .findAny()
                    .orElse(null);
            sh.remove(last);
            return "The element was deleted";
        } else {
            return "Collection is empty";
        }
    }

    /**
     * Remove first item form collection.
     */
    String remove_first(Connection c) { // Correct
        Shelter first = sh.stream()
                .sorted(Shelter::compareTo)
                .findFirst()
                .orElse(null);

        if (first != null) {
            sh.remove(first);
            return "The element was deleted";
        } else {
            return "Collection is empty";
        }
    }

    /**
     * Remove the item{name : position}
     */
    String remove(String object, Connection c) throws JsonSyntaxException, NullPointerException{
           Shelter remove_object = builder.fromJson(object, Shelter.class);

           int size = sh.size();

           if (sh.contains(remove_object)) {
               sh.remove(remove_object);
           } else {
               return "There isn't this shelter in collection";
           }

           if (size != sh.size()) {
               return "Shelter is deleted";
           } else {
               return "Shelter isn't deleted";
           }
    }

    /**
     * Add item if it's the biggest Shelter in collection
     */

    String addIfMax(String object, Connection c) {
        Shelter compare_object = builder.fromJson(object, Shelter.class);
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
        } catch (IOException e) {
            System.out.println("File hasn't been saved.");
        }

        System.out.println("\nFile has been saved.");
    }

    String save (String filename) {
        File file = new File(filename.trim());

        try {
            writeInFile(file);
        } catch (IOException e) {
            return "File isn't saved.";
        }

        return "File is saved.";
    }

    void saveOnServer() {
        try{
            File file = new File("Downloads/backup.xml");
            writeInFile(file);
        } catch (IOException e) {
            System.out.println("File hasn't been saved.");
        }
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
            bfr.write("\t\t<Date>" + shelter.getDate() + "</Date>\n");
            bfr.write("\t</Shelter>\n");
        }
        bfr.write("</ShelterList>\n");
        bfr.close();
    }
}

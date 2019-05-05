import java.util.*;

public class Main {
        static class Start {
            static void story(String file) {
                Scanner in = new Scanner(System.in);
                Random rand = new Random();
                Human alice = new Human((int) ((rand.nextDouble()) * 20),"Alice","Female", PhysicalStatus.WEAK);
                Dog dog = new Dog(40,"Stafford", PhysicalStatus.USUAL);

                ListOfShelters list = new ListOfShelters(file);

                try {
                    System.out.println(alice.getX());
                    Shelter theClosestShelter = alice.shelterNearME(list.sh);
                    alice.runTo(theClosestShelter);
                    dog.startBark();
                    dog.startAttacks(new RealObject(40,"Stick"));
                    alice.runFrom(dog);
                    dog.finishBark();
                } catch(NullPointerException e) {
                    throw new NpE();
                } catch(NpE e) {
                    System.out.println("Error : " + e);
                }

                Runtime.getRuntime().addShutdownHook(new Thread(list::save)); // Позваляет сохранить файл при аврийном завершении программы

                System.out.println("\nHello! " +
                        "It's a console.\n" +
                        "List of command: remove_last, remove {element}, show, add_if_max {element}, " +
                        "remove_first, info, add {element}");
                System.out.println("Enter 'help' if want to the list of command again.");

                ArrayList<String> command = new ArrayList<>();
                command.add("remove_last");
                command.add("add");
                command.add("remove_first");
                command.add("show");
                command.add("info");
                command.add("remove");
                command.add("add_if_max");
                command.add("exit");
                command.add("help");
                command.add("sort");
/*
                lable:
                while (true) {
                    System.out.print("-> ");
                    String s = in.next();
                    if (command.contains(s)) {
                        switch (s) {
                            case "remove_last":
                                list.remove_last();
                                break;
                            case "add":
                                list.add(in.nextLine());
                                break;
                            case "remove_first":
                                System.out.println(list.remove_first());
                                break;
                            case "show":
                                System.out.println(list.show());
                                break;
                            case "info":
                                list.info();
                                break;
                            case "remove":
                                list.remove(in.nextLine());
                                break;
                            case "add_if_max":
                                list.addIfMax(in.nextLine());
                                break;
                            case "exit":
                                list.save();
                                break lable;
                            case "help":
                                System.out.println("List of command: remove_last, " +
                                        "remove {element}, show, add_if_max {element}, " +
                                        "remove_first, info, add {element}");
                                break;
                            case "sort":
                                System.out.println(list.sort());
                                break;
                        }
                    } else {
                        System.out.println("It isn't a command. Please enter 'help' to show list of command.");
                    }
                }*/
            }
        }

    /**
     * Start and End method.
     * @param args First args is the name of XML file
     */
    public static void main(String[] args) {
        try {
            Start.story(args[0]);
            Finish printend = () -> System.out.println("\nHAPPY END.");
            printend.printEnd();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You didn't set the start file-. It must be 'data.xml'");
            System.exit(0);
        }
    }
}

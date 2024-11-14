package traffic;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the traffic management system!");
        Scanner scanner = new Scanner(System.in);
        SystemState stateOfSystem = SystemState.NOT_STARTED;

        int numberOfRoads = handleInput(scanner, "Input the number of roads: ");
        int interval = handleInput(scanner, "Input the interval: ");

        String[] roads = new String[numberOfRoads];
        int currentFront = 0;
        int currentRear = 0;

        QueueThread queueThread = new QueueThread(numberOfRoads, interval, roads);
        queueThread.setName("QueueThread");
        queueThread.start();

        boolean stopProgram = false;
        while (!stopProgram) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    boolean isAdded = addRoad(roads, currentRear, scanner);
                    if (isAdded) {
                        if (currentRear == roads.length - 1) currentRear = 0;
                        else
                            currentRear++;
                        queueThread.setRoads(roads);
                    }
                    clearConsole(scanner);
                    break;
                case "2":
                    boolean isRemoved = removeRoad(roads, currentFront);
                    if (isRemoved) {
                        if (currentFront == roads.length - 1) currentFront = 0;
                        else currentFront++;
                        queueThread.setCurrentRoadIndex(currentFront);
                    }
                    clearConsole(scanner);
                    break;
                case "3":
                    if (stateOfSystem == SystemState.NOT_STARTED){

                        stateOfSystem = SystemState.STARTED;
                    }
                    startSystemState(queueThread, scanner);
                    break;
                case "0":
                    System.out.println("Bye!");
                    stopProgram = true;
                    queueThread.interrupt();
                    break;
                default:
                    System.out.println("Incorrect option");
                    clearConsole(scanner);
            }
        }
        queueThread.interrupt();
    }



    private static boolean addRoad(String[] roads, int currentRear, Scanner scanner) {
        System.out.print("Input road name: ");
        String roadName = scanner.nextLine();

        if (roads[currentRear] != null) {
            System.out.println("Queue is full");
            return false;
        }
        roads[currentRear] = roadName;

        System.out.println(roadName + " Added!");
        return true;
    }

    private static boolean removeRoad(String[] roads, int currentFront) {
        if (roads[currentFront] == null) {
            System.out.println("Queue is empty");
            return false;
        }
        System.out.println(roads[currentFront] + " deleted!");
        roads[currentFront] = null;
        return true;
    }

    private static void startSystemState(QueueThread queueThread, Scanner scanner) {
        queueThread.activateSystemState();
        clearConsole(scanner);
        queueThread.deactivateSystemState();
    }

    private static int handleInput(Scanner scanner, String prompt) {
        int result;
        System.out.print(prompt);
        while (true) {
            try {
                result = Integer.parseInt(scanner.nextLine());
                if (result <= 0) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print("Error! Incorrect input. Try again: ");
            }
        }
        return result;
    }

    public static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Add road");
        System.out.println("2. Delete road");
        System.out.println("3. Open system");
        System.out.println("0. Quit");
    }

    private static void clearConsole(Scanner scanner) {
        scanner.nextLine();
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ignored) {

        }
    }

}

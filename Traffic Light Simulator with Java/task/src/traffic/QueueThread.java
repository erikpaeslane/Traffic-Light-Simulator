package traffic;

public class QueueThread extends Thread {

    private final int numberOfRoads;
    private final int interval;
    private int currentTime;
    private int activeRoadIndex;
    private String[] roads;
    private int timePassed;
    private volatile boolean isSystemActive;

    public QueueThread(int numberOfRoads, int interval, String[] roads) {
        this.numberOfRoads = numberOfRoads;
        this.interval = interval;
        this.roads = roads;
        this.timePassed = 1;
        this.isSystemActive = false;
        this.currentTime = interval+1;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                timePassed++;
                currentTime--;
                if (isSystemActive) {
                    System.out.println("! " + timePassed + "s. have passed since system startup !");
                    System.out.println("! Number of roads: " + numberOfRoads);
                    System.out.println("! Interval: " + interval);
                }
                if (currentTime == 0)
                    changeCurrentRoad();
                if (isSystemActive){
                    if (isAnyRoadExists())
                        printRoads();
                    System.out.println("! Press \"Enter\" to open menu !");
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private boolean isAnyRoadExists() {
        for (String road : roads) {
            if (road != null)
                return true;
        }
        return false;
    }

    private void printRoads() {
        System.out.println();

        for (int i = 0; i < numberOfRoads; i++) {
            if (roads[i] != null) {
                if (i == activeRoadIndex) {
                    System.out.println("Road \""  + roads[i]
                            + "\" will be" + "\u001B[32m" + "open for " + currentTime + "s." + "\u001B[0m");
                } else {
                    int k = findCoefficient(i);
                    System.out.println("Road \"" + roads[i] + "\" will be " + "\u001B[31m"
                            + "closed for " + (interval * (k-1) + currentTime) + "s." + "\u001B[0m");
                }
            }
        }
        System.out.println();
    }

    private int findCoefficient(int currentRoadIndex) {
        int k = 0;
        int current = activeRoadIndex;
        while (current != currentRoadIndex) {
            current = (current+1) % numberOfRoads;
            k++;
        }
        return k;
    }

    private void changeCurrentRoad() {
        if (isAnyRoadExists()) {
            do {
                if (activeRoadIndex == roads.length - 1) activeRoadIndex = 0;
                else activeRoadIndex++;

            } while (roads[activeRoadIndex] == null);
        }
        currentTime = interval;
    }

    public int getNumberOfRoads() {
        return numberOfRoads;
    }

    public int getInterval() {
        return interval;
    }

    public void activateSystemState() {
        isSystemActive = true;
    }

    public void deactivateSystemState() {
        isSystemActive = false;
    }

    public void setRoads(String[] roads) {
        this.roads = roads;
    }

    public void setCurrentRoadIndex(int currentRoadIndex) {
        this.activeRoadIndex = currentRoadIndex;
    }
}

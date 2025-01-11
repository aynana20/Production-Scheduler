package pack1;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        Scheduler scheduler = new Scheduler();

        // Load initial stock
        Map<String, Integer> initialStock = csvReader.readInitialStock("src\\pack1\\initial_stock.csv");
        scheduler.initializeIngredients(initialStock);

        // Load transit information
        csvReader.readTransit("src\\pack1\\in_transit.csv", scheduler);

        // Load machine details
        csvReader.readMachines("src\\pack1\\machines.csv", scheduler);

        // Load finished goods details
        csvReader.readFinishedGoods("src\\pack1\\finished_goods.csv", scheduler);

        // Load demand for finished goods
        Map<String, Integer> demand = csvReader.readDemand("src\\pack1\\demand.csv");
        scheduler.addDemand(demand);

        // Process daily schedules
        for (int day = 1; day <= 8; day++) {
            scheduler.processDailySchedule(day);
        }
    }
}

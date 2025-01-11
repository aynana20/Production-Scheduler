package pack1;

import java.io.*;
import java.util.*;

public class CSVReader {
    public Map<String, Integer> readInitialStock(String filePath) {
        Map<String, Integer> stock = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                stock.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void readTransit(String filePath, Scheduler scheduler) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int day = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String ingredient = parts[0];
                int qty = Integer.parseInt(parts[2]);
                Map<String, Integer> transitData = new HashMap<>();
                transitData.put(ingredient, qty);
                scheduler.addTransit(day, transitData);
                day++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMachines(String filePath, Scheduler scheduler) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String machineName = parts[0];
                int availableHours = Integer.parseInt(parts[1]);
                Machine machine = new Machine(machineName, availableHours);
                scheduler.addMachine(machine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFinishedGoods(String filePath, Scheduler scheduler) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String fgName = parts[0];
                String ingredients = parts[1];
                int productionTime = Integer.parseInt(parts[2]);
                FinishedGood fg = new FinishedGood(fgName, ingredients, productionTime);
                scheduler.addFinishedGood(fg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> readDemand(String filePath) {
        Map<String, Integer> demand = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                demand.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return demand;
    }
}

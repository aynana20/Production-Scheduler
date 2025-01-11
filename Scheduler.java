package pack1;
import java.util.*;


public class Scheduler {
    private Map<String, Integer> ingredientsStock;
    private Map<String, FinishedGood> finishedGoods;
    private Map<String, Machine> machines;
    private Map<Integer, Map<String, Integer>> ingredientTransit;
    private Map<String, Integer> demand;

    private Map<String, Integer> totalFGProduced; // Track total FG produced

    public Scheduler() {
        this.ingredientsStock = new HashMap<>();
        this.finishedGoods = new HashMap<>();
        this.machines = new HashMap<>();
        this.ingredientTransit = new HashMap<>();
        this.demand = new HashMap<>();
        this.totalFGProduced = new HashMap<>();
    }

    public void initializeIngredients(Map<String, Integer> initialStock) {
        ingredientsStock.putAll(initialStock);
    }

    public void addTransit(int day, Map<String, Integer> transitItems) {
        ingredientTransit.put(day, transitItems);
    }

    public void addMachine(Machine machine) {
        machines.put(machine.getName(), machine);
    }

    public void addFinishedGood(FinishedGood fg) {
        finishedGoods.put(fg.getName(), fg);
    }

    public void addDemand(Map<String, Integer> totalDemand) {
        demand.putAll(totalDemand);
    }

    public void processDailySchedule(int day) {
        // Update stock with transit arrivals
        Map<String, Integer> transitForToday = ingredientTransit.getOrDefault(day, new HashMap<>());
        transitForToday.forEach((ingredient, qty) -> ingredientsStock.put(ingredient, ingredientsStock.getOrDefault(ingredient, 0) + qty));

        // Print ingredients at the start of the day
        System.out.println("Day " + day + ":");
        System.out.println("Ingredients: " + ingredientsStock);

        // Initialize machine task tracking
        Map<String, Map<String, Integer>> machineTasks = new HashMap<>();

        for (String machineName : machines.keySet()) {
            machineTasks.put(machineName, new HashMap<>()); // Initialize machine task list
        }

        // Process the finished goods based on overall demand
        for (String fgName : demand.keySet()) {
            FinishedGood fg = finishedGoods.get(fgName);
            if (fg != null && canProduce(fg)) {
                int unitsToProduce = demand.get(fgName);
                for (Machine machine : machines.values()) {
                    if (machine.canProduce(fg)) {
                        // Check if enough ingredients are available for the required production
                        int maxUnits = machine.schedule(fg, unitsToProduce, ingredientsStock);
                        if (maxUnits > 0) {
                            // Record the task
                            machineTasks.get(machine.getName()).put(fgName, maxUnits);
                            // Update stock after production
                            ingredientsStock = fg.consumeIngredients(maxUnits, ingredientsStock);

                            // Track total FG produced
                            totalFGProduced.put(fgName, totalFGProduced.getOrDefault(fgName, 0) + maxUnits);
                        }
                    }
                }
            }
        }

        // Print machine task allocations for the day with FG details
        machines.forEach((machineName, machine) -> {
            System.out.println("Machine " + machineName + ":");
            Map<String, Integer> tasks = machineTasks.get(machineName);
            tasks.forEach((fgName, units) -> {
                System.out.println("  " + fgName + " x " + units + " units");
            });
        });

        // Print final ingredient stock for the day
        System.out.println("Ingredients at the end of the day:");
        ingredientsStock.forEach((ingredient, qty) -> {
            if (qty < 0) {
                System.out.println(ingredient + ": 0"); // If ingredient is negative, set it to 0
            } else {
                System.out.println(ingredient + ": " + qty);
            }
        });

        // Print total finished goods produced for the day
        System.out.println("\nTotal Finished Goods Produced (Day " + day + "):");
        totalFGProduced.forEach((fgName, totalUnits) -> {
            System.out.println(fgName + ": " + totalUnits + " units");
        });

        // Print demand fulfillment
        System.out.println("\nDemand Fulfillment:");
        demand.forEach((fgName, requiredUnits) -> {
            int producedUnits = totalFGProduced.getOrDefault(fgName, 0);
            System.out.println(fgName + ": " + producedUnits + "/" + requiredUnits + " ("
                    + (producedUnits >= requiredUnits ? "Fulfilled" : "Not Fulfilled") + ")");
        });
        System.out.println();
    }

    private boolean canProduce(FinishedGood fg) {
        Map<String, Integer> requiredIngredients = fg.getIngredientsRequired();
        for (String ingredient : requiredIngredients.keySet()) {
            if (ingredientsStock.getOrDefault(ingredient, 0) < requiredIngredients.get(ingredient)) {
                return false; // Cannot produce if ingredients are insufficient
            }
        }
        return true;
    }

    // Method to print total FG produced across all days after processing all days
    public void printTotalProductionStatus() {
        System.out.println("Total Finished Goods Produced (After 8 Days):");
        totalFGProduced.forEach((fgName, totalUnits) -> {
            System.out.println(fgName + ": " + totalUnits + " units");
        });

        System.out.println("\nDemand Fulfillment (Overall):");
        demand.forEach((fgName, requiredUnits) -> {
            int producedUnits = totalFGProduced.getOrDefault(fgName, 0);
            System.out.println(fgName + ": " + producedUnits + "/" + requiredUnits + " ("
                    + (producedUnits >= requiredUnits ? "Fulfilled" : "Not Fulfilled") + ")");
        });
    }
}

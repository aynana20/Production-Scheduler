package pack1;
import java.util.*;

public class Machine {
    private String name;
    private int availableHours;

    public Machine(String name, int availableHours) {
        this.name = name;
        this.availableHours = availableHours;
    }

    public String getName() {
        return name;
    }

    public int getAvailableHours() {
        return availableHours;
    }

    public boolean canProduce(FinishedGood fg) {
        return availableHours >= fg.getProductionTime();
    }

    public int schedule(FinishedGood fg, int unitsToProduce, Map<String, Integer> ingredientsStock) {
        int maxUnits = Math.min(unitsToProduce, availableHours / fg.getProductionTime());
        return maxUnits;
    }
}

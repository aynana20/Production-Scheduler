package pack1;

import java.util.*;

public class FinishedGood {
    private String name;
    private Map<String, Integer> ingredientsRequired;
    private int productionTime;

    public FinishedGood(String name, String ingredients, int productionTime) {
        this.name = name;
        this.ingredientsRequired = new HashMap<>();
        String[] ingredientsArray = ingredients.split(";");
        for (String ingredient : ingredientsArray) {
            String[] parts = ingredient.split(":");
            this.ingredientsRequired.put(parts[0], Integer.parseInt(parts[1]));
        }
        this.productionTime = productionTime;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getIngredientsRequired() {
        return ingredientsRequired;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public Map<String, Integer> consumeIngredients(int units, Map<String, Integer> ingredientsStock) {
        Map<String, Integer> updatedStock = new HashMap<>(ingredientsStock);
        for (String ingredient : ingredientsRequired.keySet()) {
            int requiredQty = ingredientsRequired.get(ingredient) * units;
            updatedStock.put(ingredient, updatedStock.getOrDefault(ingredient, 0) - requiredQty);
        }
        return updatedStock;
    }
}

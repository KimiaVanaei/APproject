import org.neo4j.driver.*;
import java.util.*;

public class Neo4jFinder {
    private static final Neo4jConnector neo4j = new Neo4jConnector();

    public static void search(Scanner scanner, PrintAndSaveNeo saver, PrintAndSaveOllama olSaver) {

        System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                "Do you want to filter by ingredients? (yes/no)"));
        String ingredientChoice = scanner.nextLine().trim().toLowerCase();
        List<String> ingredients = new ArrayList<>();

        if (ingredientChoice.equals("yes")) {
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Enter the ingredients you have (comma-separated):"));
            String input = scanner.nextLine();
            ingredients = Arrays.asList(input.split(","));
        }

        String tag;
        List<String> validTags = Arrays.asList("soup", "random", "sides", "american", "dessert", "fruit", "salad",
                                                "desert food", "mediterranean", "breakfast", "non-dessert", "french",
                                                "non_vegetarian", "vegan", "seafood");
        while (true) {
            System.out.print(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Select a tag: "));
            System.out.println(validTags);
            tag = scanner.nextLine().trim();
            if (validTags.contains(tag)) {
                break;
            } else {
                System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                        "Invalid tag. Please enter a valid tag from the list."));
            }

        }

        List<Map<String, Object>> recipes = neo4j.getFilteredRecipes(ingredients, tag);

        if (recipes.isEmpty()) {
            System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                    "No recipes found with the given filters. Asking Ollama for your request...\n"));
            String response = OllamaConversation.chat(Neo4jHelperReq.request(tag, String.valueOf(ingredients)));
            olSaver.exe(response, scanner);
            return;
        }

        List<Integer> validRecipeIds = new ArrayList<>();
        System.out.println(ColorUtils.applyColor(ColorUtils.BLUE, ColorUtils.BOLD +
                "I have found the following recipes:"));
        for (Map<String, Object> recipe : recipes) {
            System.out.println("ID: " + recipe.get("recipe_id") + " | Name: " + recipe.get("recipe_name"));
            int id = ((Number) recipe.get("recipe_id")).intValue();
            validRecipeIds.add(id);
        }

        int recipeId;
        while (true) {
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Enter the Recipe ID you want to view:"));
            try {
                recipeId = Integer.parseInt(scanner.nextLine().trim());
                if (validRecipeIds.contains(recipeId)) {
                    break;
                } else {
                    System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                            "Recipe ID not found. Please enter a valid Recipe ID from the list."));
                }
            } catch (NumberFormatException e) {
                System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                        "Invalid input. Please enter a numeric Recipe ID."));
            }
        }

        RecipeDetails recipeDetails = neo4j.getRecipeDetails(recipeId);
        if (recipeDetails != null) {
            saver.exe(recipeDetails.getTitle(), recipeDetails.getIngredients(), recipeDetails.getInstructions(), scanner);
        }

    }
}

import java.sql.*;
import java.util.*;
import java.util.regex.*;
import org.json.JSONArray;
import org.json.JSONException;

public class TestSQL {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/five_k";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";


    public static RecipeDetails searchRecipes(List<String> userIngredients, Scanner scanner) {
        if (userIngredients.isEmpty()) {
            System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                    "No ingredients provided."));
            return null;
        }

        List<String> matchingRecipes = new ArrayList<>();
        List<Integer> recipeIds = new ArrayList<>();
        RecipeDetails selectedRecipe = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            StringBuilder query = new StringBuilder("SELECT id, Title, Ingredients, Instructions FROM recipes WHERE ");
            List<String> conditions = new ArrayList<>();
            for (String ingredient : userIngredients) {
                conditions.add("Ingredients LIKE ?");
            }
            query.append(String.join(" OR ", conditions));

            try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
                for (int i = 0; i < userIngredients.size(); i++) {
                    stmt.setString(i + 1, "%" + userIngredients.get(i) + "%");
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("Title");
                    String ingredients = rs.getString("Ingredients");
                    String instructions = rs.getString("Instructions");

                    // Normalize and parse ingredients from the database
                    String normalizedIngredientsFromDb = normalizeIngredient(ingredients.trim());

                    long matchCount = userIngredients.stream()
                            .filter(normalizedIngredientsFromDb::contains)
                            .count();

                    if (matchCount >= 1) {
                        matchingRecipes.add(title);
                        recipeIds.add(id);
                    }
                }

                // If matching recipes are found
                if (!matchingRecipes.isEmpty()) {
                    System.out.println(ColorUtils.applyColor(ColorUtils.BLUE, ColorUtils.BOLD +
                            "I have found the following recipes:"));
                    for (int i = 0; i < matchingRecipes.size(); i++) {
                        System.out.println("ID: " + recipeIds.get(i) + " - " + matchingRecipes.get(i));
                    }

                    boolean validId = false;
                    int choice = -1;

                    // Keep asking the user for the ID until a valid one is entered
                    while (!validId) {
                        System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                                "\nPlease enter the ID of the recipe you want to view:"));

                        if (scanner.hasNextInt()) {
                            choice = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline

                            if (recipeIds.contains(choice)) {
                                validId = true;
                                selectedRecipe = displayRecipeDetails(choice, conn);
                            } else {
                                System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                                        "Invalid ID. Please try again."));
                            }
                        } else {
                            System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                                    "Invalid input. Please enter a valid recipe ID."));
                            scanner.next();
                        }
                    }

                } else {
                    System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                            "No matching recipes found. Asking Ollama for your request...\n"));

                    String response = OllamaConversation.chat(DBhelperReq.request(String.valueOf(userIngredients)));
                    System.out.println(response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selectedRecipe;
    }

    private static RecipeDetails displayRecipeDetails(int recipeId, Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT Title, Ingredients, Instructions FROM recipes WHERE id = ?")) {
            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("Title");
                String ingredients = rs.getString("Ingredients");
                String instructions = rs.getString("Instructions");

                // Remove brackets from ingredients string
                String ingredientsFormatted = parseIngredients(ingredients);

                System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "\n🍽️ Recipe: "));
                System.out.println(title);
                System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "🥗 Ingredients: "));
                System.out.println(ingredientsFormatted);
                System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "📝 Instructions: "));
                System.out.println(instructions);

                return new RecipeDetails(title, ingredientsFormatted, instructions);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String normalizeIngredient(String ingredient) {
        return ingredient.replaceAll(
                        "(?i)" +
                                "\\s*\\([^)]*\\)" +
                                "|" +
                                ",.*" +
                                "|" +
                                "\\b[0-9]+" +
                                "[½¼¾/\\-]*" +
                                "\\s*" +
                                "(tsp\\.?|tbsp\\.?|cup(s?)|lb\\.?|oz\\.?|g\\.?|ml\\.?|kg\\.?|pound(s?)|pinch\\.?|slice\\.?|clove(s?)|ounce\\.?|piece|bunch|chopped|small|large|medium|ground|freshly|fresh|pure|tablespoon(s?)|teaspoon(s?))?" +
                                "\\b",
                        "")
                .trim()
                .toLowerCase();
    }

    private static String parseIngredients(String ingredients) {
        try {
            // If ingredients are in JSON format, parse them
            JSONArray jsonArray = new JSONArray(ingredients);
            StringBuilder ingredientString = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i > 0) ingredientString.append(", ");  // Add a comma and space between items
                ingredientString.append(jsonArray.getString(i));
            }
            return ingredientString.toString();
        } catch (JSONException e) {
            String cleanIngredients = ingredients.replaceAll("[\\[\\]']", "").replaceAll("\\s*,\\s*", ", ").trim();
            return cleanIngredients;
        }
    }
}

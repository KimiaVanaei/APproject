import java.sql.*;
import java.util.*;
import java.util.regex.*;
import org.json.JSONArray;
import org.json.JSONException;

public class TestSQL {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/five_k";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                            "Enter the ingredients you have (comma separated): "));
        String input = scanner.nextLine();
        List<String> userIngredients = Arrays.asList(input.split(","));

        // Normalize the user's ingredients
        List<String> normalizedUserIngredients = new ArrayList<>();
        for (String ingredient : userIngredients) {
            normalizedUserIngredients.add(normalizeIngredient(ingredient.trim()));
        }

        // Query the database for recipes that match
        searchRecipes(normalizedUserIngredients, scanner);
    }

    public static void searchRecipes(List<String> userIngredients, Scanner scanner) {
        if (userIngredients.isEmpty()) {
            System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                    "No ingredients provided."));
            return;
        }

        List<String> matchingRecipes = new ArrayList<>();
        List<Integer> recipeIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Build a SQL query dynamically
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

                // Collect titles and IDs of matching recipes
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("Title");
                    String ingredients = rs.getString("Ingredients");
                    String instructions = rs.getString("Instructions");

                    // Parse and normalize ingredients
                    List<String> recipeIngredients = parseIngredients(ingredients);
                    List<String> normalizedRecipeIngredients = new ArrayList<>();
                    for (String ingredient : recipeIngredients) {
                        normalizedRecipeIngredients.add(normalizeIngredient(ingredient));
                    }

                    long matchCount = userIngredients.stream()
                            .filter(normalizedRecipeIngredients::contains)
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

                            if (recipeIds.contains(choice)) {
                                validId = true;
                                displayRecipeDetails(choice, conn);
                            } else {
                                System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                                        "Invalid ID. Please try again."));
                            }
                        } else {
                            // Handle the case where the user doesn't input an integer
                            System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                                    "Invalid input. Please enter a valid recipe ID."));
                            scanner.next(); // Consume the invalid input
                        }
                    }

                } else {
                    System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                            "No matching recipes found."));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void displayRecipeDetails(int recipeId, Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT Title, Ingredients, Instructions FROM recipes WHERE id = ?")) {
            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("Title");
                String ingredients = rs.getString("Ingredients");
                String instructions = rs.getString("Instructions");
                System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "\n🍽️ Recipe: "));
                System.out.println(title);
                System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "🥗 Ingredients: "));
                System.out.println(ingredients);
                System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "📝 Instructions: "));
                System.out.println(instructions);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Normalize ingredient by removing quantities and units
    private static String normalizeIngredient(String ingredient) {
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

    private static List<String> parseIngredients(String ingredients) {
        try {
            JSONArray jsonArray = new JSONArray(ingredients);
            List<String> ingredientList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ingredientList.add(jsonArray.getString(i));
            }
            return ingredientList;
        } catch (JSONException e) {
            return Arrays.asList(ingredients.replaceAll("[\\[\\]' ]", "").split(","));
        }
    }
}
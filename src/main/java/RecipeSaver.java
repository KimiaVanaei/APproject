import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeSaver {

    private static final String FILE_NAME = "saved_recipes.txt";

    public static void saveRecipe(String title, String ingredients, String instructions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("📌 Recipe saved on: " + date + "\n\n");

            writer.write("🍽️ " + title + " 🍽️\n\n");

            writer.write("🥗 Ingredients:\n");
            writer.write("------------------------------------------------------------\n");

            String[] ingredientList = ingredients.split(",");
            for (String ingredient : ingredientList) {
                writer.write("- " + ingredient.trim() + "\n");
            }

            writer.write("\n📝 Instructions:\n");
            writer.write("------------------------------------------------------------\n");

            writer.write(instructions + "\n");

            writer.write("\n\n");

        } catch (IOException e) {
            System.out.println("Error saving recipe: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example usage
        String title = "Spaghetti Carbonara";
        String ingredients = "Spaghetti, Eggs, Parmesan cheese, Pancetta, Garlic, Black pepper";
        String instructions = "1. Boil the spaghetti.\n2. Cook the pancetta.\n3. Mix eggs and cheese.\n4. Combine and serve.";

        // Save the recipe to the file
        saveRecipe(title, ingredients, instructions);
    }

}

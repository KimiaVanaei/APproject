import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Neo4jSaver {
    protected static final String FILE_NAME = "saved_recipes.txt";

    public void saveRecipe(ExtendedRecipeDetails recipe) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("📌 Neo4j Recipe saved on: " + date + "\n\n");

            writer.write("🍽️ " + recipe.getTitle() + " 🍽️\n\n");

            writer.write("📜 Description:\n");
            writer.write( recipe.getDescription() + "\n");

            writer.write("\n🥗 Ingredients:\n");
            writer.write( recipe.getIngredients() + "\n");

            writer.write("\n⏳ Total Time: "+ recipe.getTotalTime() + " minutes" + "\n");

            writer.write("\n🍵 Prep Time: " + recipe.getMinPrep() + " minutes" + "\n");

            writer.write("\n🍴 Servings: " + recipe.getServingsMin() + " - " + recipe.getServingsMax() + "\n");

            writer.write("\n📝 Instructions:\n");
            writer.write(recipe.getInstructions() + "\n");

            writer.write("\n\n");
            writer.write("-------------------------------------------------------------------------------------\n");

        } catch (IOException e) {
            System.out.println("Error saving recipe: " + e.getMessage());
        }
    }

}
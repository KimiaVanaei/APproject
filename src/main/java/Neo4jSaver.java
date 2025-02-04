import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Neo4jSaver extends RecipeSaver {
    @Override
    public void saveRecipe(String title, String ingredients, String instructions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("📌 Neo4j Recipe saved on: " + date + "\n\n");

            writer.write("🍽️ " + title + " 🍽️\n\n");

            writer.write("🥗 Ingredients:\n");
            writer.write( ingredients + "\n");


            writer.write("\n📝 Instructions:\n");
            writer.write(instructions + "\n");

            writer.write("\n\n");
            writer.write("------------------------------------------------------------\n");

        } catch (IOException e) {
            System.out.println("Error saving recipe: " + e.getMessage());
        }
    }

    @Override
    public void saveOllamaRecipe(String response) {

    }

    @Override
    public void saveMealDBRecipe(String result) {

    }
}
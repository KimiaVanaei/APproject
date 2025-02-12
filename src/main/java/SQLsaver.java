import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLsaver {
    private static final String FILE_NAME = "saved_recipes.txt";

    public void saveRecipe(String title, String ingredients, String instructions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("📌 SQL Recipe saved on: " + date + "\n\n");

            writer.write("🍽️ " + title + " 🍽️\n\n");

            writer.write("🥗 Ingredients:\n");
            writer.write( ingredients + "\n");


            writer.write("\n📝 Instructions:\n");

            writer.write(instructions + "\n");

            writer.write("\n\n");
            writer.write("-------------------------------------------------------------------------------------\n");

        } catch (IOException e) {
            System.out.println("Error saving recipe: " + e.getMessage());
        }
    }

}
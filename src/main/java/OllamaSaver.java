import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OllamaSaver {
    protected static final String FILE_NAME = "saved_recipes.txt";

    public void saveOllamaRecipe(String response) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("📌 Ollama Recipe saved on: " + date + "\n\n");
            writer.write(response);
            writer.write("\n\n");
            writer.write("------------------------------------------------------------\n");
        } catch (IOException e) {
            System.out.println("Error saving recipe: " + e.getMessage());
        }

    }
}
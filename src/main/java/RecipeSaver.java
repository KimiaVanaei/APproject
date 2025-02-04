import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

abstract class RecipeSaver {

    protected static final String FILE_NAME = "saved_recipes.txt";

    public abstract void saveRecipe(String title, String ingredients, String instructions);
    public abstract void saveMealDBRecipe(String result);
    public abstract void saveOllamaRecipe(String response);

}

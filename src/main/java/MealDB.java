import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MealDB {

    public static String fetchMealData(String urlString) {
        String response = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                responseBuilder.append(output);
            }

            conn.disconnect();
            response = responseBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return formatMealResponse(response);
    }

    // Method to format the JSON response from the API
    private static String formatMealResponse(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        // If there are no meals in the response
        if (!jsonObject.has("meals") || jsonObject.get("meals").isJsonNull()) {
            return "No meal found!";
        }

        JsonArray meals = jsonObject.getAsJsonArray("meals");
        JsonObject meal = meals.get(0).getAsJsonObject(); // Get the first meal

        String mealName = meal.get("strMeal").getAsString();
        String category = meal.get("strCategory").getAsString();
        String area = meal.get("strArea").getAsString();
        String instructions = meal.get("strInstructions").getAsString();
        String youtubeLink = meal.get("strYoutube").getAsString();
        String imageLink = meal.get("strMealThumb").getAsString();

        // Get ingredients and measurements
        StringBuilder ingredients = new StringBuilder();
        for (int i = 1; i <= 20; i++) {
            String ingredientKey = "strIngredient" + i;
            String measureKey = "strMeasure" + i;
            if (meal.has(ingredientKey) && !meal.get(ingredientKey).isJsonNull() && !meal.get(ingredientKey).getAsString().trim().isEmpty()) {
                ingredients.append(meal.get(measureKey).getAsString())
                        .append(" ").append(meal.get(ingredientKey).getAsString())
                        .append("\n");
            }
        }

        // Return the formatted response
        return String.format(
                "🍽️ Meal: %s\n📌 Category: %s\n🌍 Cuisine: %s\n\n📝 Instructions:\n%s\n\n🥗 Ingredients:\n%s\n🎥 Watch Recipe: %s\n🖼️ Image: %s",
                mealName, category, area, instructions, ingredients.toString(), youtubeLink, imageLink
        );
    }

    // Search meal by name
    public static String searchMealByName(String foodName) {
        String encodedFoodName = URLEncoder.encode(foodName, StandardCharsets.UTF_8);
        String urlString = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + encodedFoodName;
        return fetchMealData(urlString);
    }

    // List all meals by first letter
    public static String listMealsByFirstLetter(String letter) {
        String urlString = "https://www.themealdb.com/api/json/v1/1/search.php?f=" + letter;
        return fetchMealData(urlString);
    }


    // Lookup a single random meal
    public static String lookupRandomMeal() {
        String urlString = "https://www.themealdb.com/api/json/v1/1/random.php";
        return fetchMealData(urlString);
    }

}

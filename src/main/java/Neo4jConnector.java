import java.util.ArrayList;
import java.util.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import static org.neo4j.driver.Values.parameters;

class Neo4jConnector {
    private final Driver driver;

    public Neo4jConnector() {
        String uri = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "TPAGUrZZk7gMJtY62tCQ8tpJ53SOc8EmV4zMvPFy55Q";
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public List<Map<String, Object>> getFilteredRecipes(List<String> ingredients, String tag) {
        try (Session session = driver.session()) {
            String query = "MATCH (r:Recipe)-[:HAS_TAG]->(t:Tag {tag_name: $tag}) ";
            if (!ingredients.isEmpty()) {
                query += "WHERE EXISTS { MATCH (r)-[:HAS_INGREDIENT]->(i:Ingredient) WHERE i.ingredient_name IN $ingredients } ";
            }
            query += "RETURN r.recipe_id AS recipe_id, r.recipe_name AS recipe_name";

            String finalQuery = query;
            return session.readTransaction(tx -> {
                List<Map<String, Object>> results = new ArrayList<>();
                Result result = tx.run(finalQuery, parameters("tag", tag, "ingredients", ingredients));
                while (result.hasNext()) {
                    results.add(result.next().asMap());
                }
                return results;
            });
        }
    }

    public ExtendedRecipeDetails getRecipeDetails(int recipeId) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                String query = "MATCH (r:Recipe {recipe_id: $recipeId}) " +
                        "OPTIONAL MATCH (r)-[:HAS_INGREDIENT]->(i:Ingredient) " +
                        "RETURN r.recipe_name AS title, r.directions AS instructions, " +
                        "r.description AS description, r.min_prep AS min_prep, " +
                        "r.total_time AS total_time, r.servings_min AS servings_min, " +
                        "r.servings_max AS servings_max, " +
                        "COLLECT(i.ingredient_name) AS ingredients";

                Result result = tx.run(query, parameters("recipeId", recipeId));
                if (result.hasNext()) {
                    Record record = result.next();
                    List<String> ingredientsList = record.get("ingredients").asList(Value::asString);
                    String ingredients = String.join(", ", ingredientsList);
                    return new ExtendedRecipeDetails(
                            record.get("title").asString(),
                            ingredients,
                            record.get("instructions").asString(),
                            record.get("description").asString(),
                            record.get("min_prep").asString(),
                            record.get("total_time").asInt(),
                            record.get("servings_min").asString(),
                            record.get("servings_max").asString()
                    );
                }
                return null;
            });
        }
    }

    public void close() {
        driver.close();
    }
}

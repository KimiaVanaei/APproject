public class RecipeDetails {
    private String title;
    private String ingredients;
    private String instructions;

    public RecipeDetails(String title, String ingredients, String instructions) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getTitle() {
        return title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

}

import java.util.List;

public class ExtendedRecipeDetails extends RecipeDetails {
    private String description;
    private int totalTime;
    private String minPrep;
    private String servingsMin;
    private String servingsMax;


    public ExtendedRecipeDetails(String title, String ingredients, String instructions, String description,
                                 String minPrep, int totalTime, String servingsMin, String servingsMax) {
        super(title, ingredients, instructions);
        this.description = description;
        this.totalTime = totalTime;
        this.minPrep = minPrep;
        this.servingsMin = servingsMin;
        this.servingsMax = servingsMax;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public String getMinPrep() {
        return minPrep;
    }

    public String getServingsMin() {
        return servingsMin;
    }

    public String getServingsMax() {
        return servingsMax;
    }

}
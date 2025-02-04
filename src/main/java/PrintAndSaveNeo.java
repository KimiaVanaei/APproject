import java.util.Scanner;

public class PrintAndSaveNeo {

    public void exe(ExtendedRecipeDetails recipe, Scanner scanner) {
        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "\n🍽️ Recipe: "));
        System.out.println(recipe.getTitle());

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "📜 Description: "));
        System.out.println(recipe.getDescription());

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "🥗 Ingredients: "));
        System.out.println(recipe.getIngredients());

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "⏳ Total Time: "));
        System.out.println(recipe.getTotalTime() + " minutes");

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "🍵 Prep Time: "));
        System.out.println(recipe.getMinPrep() + " minutes");

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "🍴 Servings: "));
        System.out.println(recipe.getServingsMin() + " - " + recipe.getServingsMax());

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "📝 Instructions: "));
        System.out.println(recipe.getInstructions());

        while (true) {
            System.out.println();
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Would you like to save this recipe? (yes/no): "));

            String saveChoice = scanner.nextLine().trim().toLowerCase();

            if (saveChoice.equals("yes")) {
                Neo4jSaver saver = new Neo4jSaver();
                saver.saveRecipe(recipe);
                System.out.println(ColorUtils.applyColor(ColorUtils.GREEN, ColorUtils.BOLD +
                        "Recipe saved successfully!"));
                break;
            } else if (saveChoice.equals("no")) {
                break;
            } else {
                System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                        "Invalid input. Please enter 'yes' or 'no'."));
            }
        }
    }

}
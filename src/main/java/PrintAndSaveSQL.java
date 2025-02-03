import java.util.Scanner;

public class PrintAndSaveSQL extends PrintAndSave {
    @Override
    public void exe (String title, String ingredients, String instructions, Scanner scanner) {

        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "\n🍽️ Recipe: "));
        System.out.println(title);
        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "🥗 Ingredients: "));
        System.out.println(ingredients);
        System.out.print(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD + "📝 Instructions: "));
        System.out.println(instructions);
        while (true) {
            System.out.println();
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Would you like to save this recipe? (yes/no): "));

            String saveChoice = scanner.nextLine().trim().toLowerCase();

            if (saveChoice.equals("yes")) {
                SQLsaver saver = new SQLsaver();
                saver.saveRecipe(title, ingredients, instructions);
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
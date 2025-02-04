import java.util.Scanner;

public class PrintAndSaveOllama extends PrintAndSave {

    @Override
    public void exe (String title, String ingredients, String instructions, Scanner scanner) {

    }

    @Override
    public void exe2(String result, Scanner scanner) {
        System.out.println(result);

        while (true) {
            System.out.println();
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Would you like to save this recipe? (yes/no): "));

            String saveChoice = scanner.nextLine().trim().toLowerCase();

            if (saveChoice.equals("yes")) {
                OllamaSaver saver = new OllamaSaver();
                saver.saveOllamaRecipe(result);
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
import java.util.Scanner;

public class PrintAndSaveOllama {
    public void exe (String response, Scanner scanner) {
        System.out.println(response);

        while (true) {
            System.out.println();
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Would you like to save this recipe? (yes/no): "));

            String saveChoice = scanner.nextLine().trim().toLowerCase();

            if (saveChoice.equals("yes")) {
                OllamaSaver saver = new OllamaSaver();
                saver.saveOllamaRecipe(response);
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
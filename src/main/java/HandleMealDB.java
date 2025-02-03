import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HandleMealDB {

    public static void search(Scanner scanner, PrintAndSaveMealDB saver) {
        System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                "🔍 Choose a MealDB search method:"));
        System.out.println(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD +
                "1️⃣ Search by meal name\n2️⃣ Search by first letter\n3️⃣ Get a random meal"));

        String choice = scanner.nextLine();
        String result = "";

        switch (choice) {
            case "1":
                System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                        "Enter the meal name: "));
                String mealName = scanner.nextLine().trim();
                result = MealDB.searchMealByName(mealName);
                break;

            case "2":
                System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                        "Enter the first letter: "));
                String firstLetter = scanner.nextLine().trim();
                if (firstLetter.length() != 1) {
                    System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                            "Please enter only a single letter. Returning to main menu."));
                    return;
                }
                result = MealDB.listMealsByFirstLetter(firstLetter);
                break;

            case "3":
                System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                        "Fetching a random meal..."));
                result = MealDB.lookupRandomMeal();
                break;

            default:
                System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                        "Invalid choice. Returning to main menu."));
                return;
        }

        System.out.println(ColorUtils.applyColor(ColorUtils.BLUE, ColorUtils.BOLD +
                "\n🍽️ Here is your meal suggestion:\n"));

        saver.exe(result, scanner);

    }

}
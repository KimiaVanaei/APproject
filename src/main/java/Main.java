import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "-------------------------------------------------------------------------"));
            System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                    "Enter the ingredients you have (comma separated), or type 'bye' to exit: "));

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                        "Goodbye! Have a great day!"));

                // Cleanup JDBC drivers to prevent lingering MySQL threads
                deregisterJDBCDrivers();

                scanner.close();
                System.exit(0);
            }

            List<String> userIngredients = Arrays.asList(input.split(","));
            RecipeDetails recipe = TestSQL.searchRecipes(userIngredients, scanner);

            if (recipe != null) {
                while (true) {
                    System.out.println();
                    System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                            "Would you like to save this recipe? (yes/no): "));

                    String saveChoice = scanner.nextLine().trim().toLowerCase();

                    if (saveChoice.equals("yes")) {
                        RecipeSaver.saveRecipe(recipe.getTitle(), recipe.getIngredients(), recipe.getInstructions());
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
    }

    private static void deregisterJDBCDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("Deregistered JDBC driver: " + driver);
            } catch (Exception e) {
                System.out.println("Error deregistering driver: " + e.getMessage());
            }
        }
    }
}

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final PrintAndSaveMealDB mealDbSaver = new PrintAndSaveMealDB();
    private static final PrintAndSaveSQL sqlSaver = new PrintAndSaveSQL();
    private static final PrintAndSaveNeo neoSaver = new PrintAndSaveNeo();
    private static final PrintAndSaveOllama olSaver = new PrintAndSaveOllama();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ColorUtils.applyColor(ColorUtils.CYAN, ColorUtils.BOLD +
                "\nWelcome to your AI Recipe Assistant!"));
        System.out.println(ColorUtils.applyColor(ColorUtils.CYAN, ColorUtils.BOLD +
                "🎉 I'm here to help you discover delicious meals based on what you have in your kitchen!" +
                " Whether you're craving comfort food, want something healthy, or need to whip up dinner with whatever’s" +
                " left in the fridge, I've got you covered 😊 Let’s get cooking! 🍳🍴"));


        while (true) {
            System.out.println(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD +
                    "\nChoose one of the three methods below, or type 'bye' to exit:"));
            System.out.println(ColorUtils.applyColor(ColorUtils.YELLOW, ColorUtils.BOLD +
                    "\n1️⃣ MealDB – Search by meal name, first letter, or get a random meal.\n2️⃣ SQL Database – Search by" +
                    " ingredients only.\n3️⃣ Neo4j Database – Search by tag (required) and optionally by ingredients."));

            System.out.println("\uD83D\uDCA1 If no results are found using your chosen method, I'll automatically check" +
                    " with Ollama to find the best match for you.\n");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                        "Goodbye! Have a great meal!"));

                // Cleanup JDBC drivers to prevent lingering MySQL threads
                deregisterJDBCDrivers();
                scanner.close();
                System.exit(0);
            }

            switch (input) {
                case "1":
                    System.out.println(ColorUtils.applyColor(ColorUtils.CYAN, ColorUtils.BOLD +
                            "You selected MealDB. Implementing MealDB search..."));
                    System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                            "\n-------------------------------------------------------------------------"));
                    HandleMealDB.search(scanner, mealDbSaver, olSaver);
                    break;

                case "2":
                    System.out.println(ColorUtils.applyColor(ColorUtils.CYAN, ColorUtils.BOLD +
                            "You selected SQL Database. Implementing SQL search..."));
                    System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                            "\n-------------------------------------------------------------------------"));
                    System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                            "Enter the ingredients you have (comma separated): "));

                    String ingr = scanner.nextLine().trim();
                    List<String> userIngredients = Arrays.asList(ingr.split(","));
                    RecipeDetails recipe = TestSQL.searchRecipes(userIngredients, scanner);
                    if (recipe != null) {
                        sqlSaver.exe(recipe.getTitle(), recipe.getIngredients(), recipe.getInstructions(), scanner);
                    } else {
                        String response = OllamaConversation.chat(DBhelperReq.request(String.valueOf(userIngredients)));
                        olSaver.exe2(response, scanner);
                    }
                    break;

                case "3":
                    System.out.println(ColorUtils.applyColor(ColorUtils.CYAN, ColorUtils.BOLD +
                            "You selected Neo4j Database. Implementing Neo4j search..."));
                    System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
                            "\n-------------------------------------------------------------------------"));
                    Neo4jFinder.search(scanner, neoSaver, olSaver);
                    break;

                default:
                    System.out.println(ColorUtils.applyColor(ColorUtils.RED, ColorUtils.BOLD +
                            "Invalid choice. Returning to main menu."));
            }
        }
    }

    private static void deregisterJDBCDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
//                System.out.println("Deregistered JDBC driver: " + driver);
            } catch (Exception e) {
                System.out.println("Error deregistering driver: " + e.getMessage());
            }
        }
    }

}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SearchDB {
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println(ColorUtils.applyColor(ColorUtils.MAGENTA, ColorUtils.BOLD +
            "Enter the ingredients you have (comma separated): "));
    String input = scanner.nextLine();
    List<String> userIngredients = Arrays.asList(input.split(","));

    // Query the database for recipes that match
    TestSQL.searchRecipes(userIngredients, scanner);
}
}
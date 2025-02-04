public class MealDBreq {
    public static String requestByName(String name) {
        String str;
        str = "Hello, can you please give me full recipe of this meal?: " + name;
        return str;
    }
    public static String requestByLetter(String letter) {
        String str;
        str = "Hello, can you please give me a full recipe of a meal that its name starts with the letter: " + letter;
        return str;
    }
}
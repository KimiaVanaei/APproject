public class DBhelperReq {

    public static String request(String ingredients) {
        String str;
        str = "Hello, can you please give me a recipe that contains these ingredients? please say (you asked for these ingredients in your recipe: ) and then type the ingredients i said, then you can type your answer. here are the ingredients:" + ingredients;
        return str;
    }

}
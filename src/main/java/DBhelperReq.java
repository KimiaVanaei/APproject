public class DBhelperReq {

    public static String request(String ingredients) {
        String str;
        str = "Hello, can you please give me a recipe that contains the following ingredients?" +
                " Please say (You asked for these ingredients in your recipe: ) and then " +
                "type the following ingredients. After that, you can type your answer. Here are the ingredients:" + ingredients;
        return str;
    }

}
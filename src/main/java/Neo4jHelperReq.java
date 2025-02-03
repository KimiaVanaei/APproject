public class Neo4jHelperReq {
    public static String request (String tag, String ingredients) {
        String str;
        str = "Hello, can you please give me a recipe that is of kind " + tag + " and contains the following ingredients?" +
                " First of all mention the food kind, then say (You asked for these ingredients in your recipe: ) and then " +
                "type the following ingredients. After that, you can type your answer. Here are the ingredients:" + ingredients;
        return str;
    }
}
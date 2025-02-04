public class Neo4jHelperReq {
    public static String request(String tag, String ingredients) {
        String str;
        if (ingredients != null) {
            str = "Hello, can you please give me a full meal recipe that is of kind \"" + tag + "\" and contains these" +
                    " ingredients?: " + ingredients;
        } else {
            str = "Hello, can you please give me a full meal recipe that is of kind \"" + tag + "\" ?";
        }

        return str;
    }
}
public class DBhelperReq {

    public static String request(String ingredients) {
        String str;
        str = "Hello, can you please give me a full meal recipe that contains all theses ingredients?: " +
                ingredients;
        return str;
    }

}
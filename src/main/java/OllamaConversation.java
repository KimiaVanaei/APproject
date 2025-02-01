import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class OllamaConversation {
    private static final String MODEL = "llama3.2:1b";
    private static final String ANS_URL = "http://localhost:11434/api/generate";
    public static String chat(String question) {
        try {
            JSONObject data = new JSONObject();
            data.put("model", MODEL);
            data.put("prompt", question);
            data.put("stream", false);

            // Send request to get the answer
            HttpURLConnection ansConnection = createPostRequest(ANS_URL, data);
            int ansStatus = ansConnection.getResponseCode();

            if (ansStatus == 200) {
                String ansResponse = readResponse(ansConnection);
                JSONObject resJson = new JSONObject(ansResponse);
                return resJson.getString("response");
            } else {
                return ("Error: " + ansStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Exception occurred while processing the request"; // Return a string in case of exception
        }
    }


    private static HttpURLConnection createPostRequest(String urlString, JSONObject payload) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        return connection;
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
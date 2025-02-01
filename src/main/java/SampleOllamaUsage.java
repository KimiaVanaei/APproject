import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class SampleOllamaUsage {

    private static final String MODEL = "llama3.2:1b";
    private static final String ANS_URL = "http://localhost:11434/api/generate";
    private static final String EMBED_URL = "http://localhost:11434/api/embeddings";


    private static double[] getEmbedding(String prompt) throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("model", MODEL);
        payload.put("prompt", prompt);

        HttpURLConnection connection = createPostRequest(EMBED_URL, payload);
        int status = connection.getResponseCode();

        if (status == 200) {
            String response = readResponse(connection);
            JSONObject responseJson = new JSONObject(response);
            JSONArray embeddingArray = responseJson.getJSONArray("embedding");

            double[] embedding = new double[embeddingArray.length()];
            for (int i = 0; i < embeddingArray.length(); i++) {
                embedding[i] = embeddingArray.getDouble(i);
            }
            return embedding;
        } else {
            System.out.println("Error: " + status);
            return null;
        }
    }


    private static double innerProduct(double[] v1, double[] v2) {
        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException("One or both vectors are null.");
        }
        if (v1.length != v2.length) {
            throw new IllegalArgumentException("Vectors must be of the same length.");
        }

        // Compute magnitudes of v1 and v2
        double magnitudeV1 = 0.0;
        double magnitudeV2 = 0.0;

        for (int i = 0; i < v1.length; i++) {
            magnitudeV1 += v1[i] * v1[i];
            magnitudeV2 += v2[i] * v2[i];
        }
        magnitudeV1 = Math.sqrt(magnitudeV1);
        magnitudeV2 = Math.sqrt(magnitudeV2);

        // Normalize the vectors
        double[] normalizedV1 = new double[v1.length];
        double[] normalizedV2 = new double[v2.length];
        for (int i = 0; i < v1.length; i++) {
            normalizedV1[i] = v1[i] / magnitudeV1;
            normalizedV2[i] = v2[i] / magnitudeV2;
        }

        double dotProduct = 0.0;
        for (int i = 0; i < normalizedV1.length; i++) {
            dotProduct += normalizedV1[i] * normalizedV2[i];
        }

        return dotProduct;  // cosine similarity
    }


    public static void main(String[] args) {
        try {
            String question = "if i give you a pdf file, can u extract info from it?";
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
                String ans = resJson.getString("response");

                // Print the answer
                System.out.println(ans);

                // Get embeddings of question and answer
                double[] v1 = getEmbedding(question);
                double[] v2 = getEmbedding(ans);

                // Compute inner product
                double innerPr = innerProduct(v1, v2);
                System.out.println("\nThe inner product of question and answer is: " + innerPr + "\n");
            } else {
                System.out.println("Error: " + ansStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility method to create a POST request
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

    // Utility method to read the response from the server
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

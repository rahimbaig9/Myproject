import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeExpertsApp {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://api.example.com/experts");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        InputStream inputStream = connection.getInputStream();
        List<Expert> experts = new ArrayList<>();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            String json = new String(buffer, 0, bytesRead);
            experts.add(Expert.fromJson(json));
        }

        PrintWriter out = new PrintWriter(System.out);
        for (Expert expert : experts) {
            out.println(expert);
        }
        out.close();
    }
}

class Expert {

    private String name;
    private int rating;
    private String price;
    private String availability;

    public Expert(String name, int rating, String price, String availability) {
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.availability = availability;
    }

    public static Expert fromJson(String json) {
        return new Expert(
            json.substring(json.indexOf("name") + 6, json.indexOf("rating")),
            Integer.parseInt(json.substring(json.indexOf("rating") + 8, json.indexOf("price"))),
            json.substring(json.indexOf("price") + 6, json.indexOf("availability")),
            json.substring(json.indexOf("availability") + 11)
        );
    }

    @Override
    public String toString() {
        return String.format("%s (%d, %s, %s)", name, rating, price, availability);
    }
}

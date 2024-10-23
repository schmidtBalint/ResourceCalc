import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ResourceCalc {

    private static final String DATASET_URL = "http://localhost:8080/v1/dataset";
    private static final String RESULT_URL = "http://localhost:8080/v1/result";

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> rawData = fetchData();
        int totalUsageTime = aggregateData(rawData);
        sendResult(totalUsageTime);
    }

    private static List<String> fetchData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATASET_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return List.of(response.body().split("\n"));
    }

    private static int aggregateData(List<String> data) {
        return data.stream()
                .mapToInt(Integer::parseInt)
                .sum();
    }

    private static void sendResult(int result) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESULT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(result)))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
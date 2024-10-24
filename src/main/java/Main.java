import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HTTPClient client = retrofit.create(HTTPClient.class);

        // Fetch dataset
        Call<DatasetResponse> call = client.fetchDatasetAsJson();
        try {
            Response<DatasetResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                DatasetResponse datasetResponse = response.body();
                // Process datasetResponse to create a Result object
                Result result = processDataset(datasetResponse);
                // Convert Result object to JSON
                String resultJson = convertResultToJson(result);

                // Send result
                Call<Void> sendCall = client.sendResult(resultJson);
                Response<Void> sendResponse = sendCall.execute();
                if (sendResponse.isSuccessful()) {
                    System.out.println("Result sent successfully.");
                } else {
                    System.err.println("Failed to send result.");
                }
            } else {
                System.err.println("Failed to fetch dataset.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Result processDataset(DatasetResponse datasetResponse) {
        // Extract the necessary data from the DatasetResponse object
        List<Event> events = datasetResponse.getEvents();

        // Create and return a Result object based on the extracted data
        Result result = new Result();
        result.setEvents(events);
        return result;
    }

    private static String convertResultToJson(Result result) {
        // Create a Gson instance
        Gson gson = new Gson();
        // Convert the Result object to a JSON string
        return gson.toJson(result);
    }
}
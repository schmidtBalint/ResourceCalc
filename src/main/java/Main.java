import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create the Gson instance with custom deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Event.class, new EventDeserializer())
                .create();

        // Create the Retrofit client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        HTTPClient service = retrofit.create(HTTPClient.class);

        try {
            // Fetch dataset
            retrofit2.Response<DatasetResponse> response = service.getDataset().execute();
            if (response.isSuccessful() && response.body() != null) {
                DatasetResponse datasetResponse = response.body();
                List<Event> events = datasetResponse.getEvents();

                // Log the number of events fetched
                System.out.println("Number of events fetched: " + events.size());

                Map<String, Long> customerUsage = calculateUsage(events);

                // Log customer usage data
                System.out.println("Customer Usage Data: " + customerUsage);

                // Create a collection of Customer objects
                Collection<Customer> customers = new ArrayList<>();
                for (Map.Entry<String, Long> entry : customerUsage.entrySet()) {
                    customers.add(new Customer(entry.getKey(), entry.getValue()));
                }

                // Log the number of customers created
                System.out.println("Number of customers created: " + customers.size());

                // Create a Result object
                Result result = new Result(customers);

                // Log the Result object
                System.out.println("Result Data: " + result);

                // Log the JSON payload
                String jsonPayload = gson.toJson(result);
                System.out.println("JSON Payload: " + jsonPayload);

                // Send the Result object
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonPayload);
                Request request = new Request.Builder()
                        .url("http://localhost:8080/v1/result")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try (Response resultResponse = client.newCall(request).execute()) {
                    if (resultResponse.isSuccessful()) {
                        System.out.println("Results sent successfully");
                    } else {
                        System.out.println("Failed to send results: " + resultResponse.code() + " - " + resultResponse.body().string());
                    }
                }
            } else {
                System.out.println("Failed to fetch dataset: " + response.code() + " - " + response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Long> calculateUsage(List<Event> events) {
        Map<String, Long> customerUsage = new HashMap<>();
        Map<String, Long> workloadStartTimes = new HashMap<>();

        for (Event event : events) {
            if ("start".equals(event.eventType())) {
                workloadStartTimes.put(event.customerId(), event.timestamp());
            } else if ("stop".equals(event.eventType())) {
                Long startTime = workloadStartTimes.remove(event.customerId());
                if (startTime != null) {
                    long duration = event.timestamp() - startTime;
                    customerUsage.merge(event.customerId(), duration, Long::sum);
                }
            }
        }

        return customerUsage;
    }
}
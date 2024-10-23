import java.util.*;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main {
    public static void main(String[] args) {
        // Erstelle den Retrofit-Client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HTTPClient service = retrofit.create(HTTPClient.class);

        try {
            // Rohdaten abrufen
            Response<List<Dataset>> response = service.getDataset().execute();
            List<Dataset> datasets = response.body();

            if (datasets != null) {
                Map<String, Long> customerUsage = calculateUsage(datasets);

                // Ergebnis senden
                for (Map.Entry<String, Long> entry : customerUsage.entrySet()) {
                    Result result = new Result(entry.getKey(), entry.getValue());
                    service.sendResult(result).execute();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Long> calculateUsage(List<Dataset> datasets) {
        Map<String, Long> customerUsage = new HashMap<>();
        Map<String, Long> workloadStartTimes = new HashMap<>();

        for (Dataset dataset : datasets) {
            if ("start".equals(dataset.eventType)) {
                workloadStartTimes.put(dataset.workloadId, dataset.timestamp);
            } else if ("stop".equals(dataset.eventType)) {
                Long startTime = workloadStartTimes.remove(dataset.workloadId);
                if (startTime != null) {
                    long duration = dataset.timestamp - startTime;
                    customerUsage.merge(dataset.customerId, duration, Long::sum);
                }
            }
        }

        return customerUsage;
    }
}

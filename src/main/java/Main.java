import java.util.*;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main {
    public static void main(String[] args) {
        // Create the Retrofit client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HTTPClient service = retrofit.create(HTTPClient.class);

        try {
            // Fetch raw data
            Response<DatasetResponse> response = service.getDataset().execute();
            DatasetResponse datasetResponse = response.body();

            if (datasetResponse != null) {
                List<Dataset> datasets = datasetResponse.getEvents();
                Map<String, Long> customerUsage = calculateUsage(datasets);

                // Send result
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
            if ("start".equals(dataset.getEventType())) {
                workloadStartTimes.put(dataset.getWorkloadId(), dataset.getTimestamp());
            } else if ("stop".equals(dataset.getEventType())) {
                Long startTime = workloadStartTimes.remove(dataset.getWorkloadId());
                if (startTime != null) {
                    long duration = dataset.getTimestamp() - startTime;
                    customerUsage.merge(dataset.getCustomerId(), duration, Long::sum);
                }
            }
        }

        return customerUsage;
    }
}
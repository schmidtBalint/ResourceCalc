import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface HTTPClient {
    @GET("/v1/dataset")
    Call<DatasetResponse> getDataset();

    @POST("/v1/result")
    Call<Void> sendResult(@Body Result result);
}

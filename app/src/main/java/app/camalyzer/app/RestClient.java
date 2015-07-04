package app.camalyzer.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.camalyzer.app.CameraActivity.ApiService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by admin on 7/3/2015.
 */
public class RestClient {

    private static final String BASE_URL = "http://api.cloudsightapi.com/";
    private ApiService apiService;

    public RestClient() {

        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        apiService = restAdapter.create(ApiService.class);

    }

    public ApiService getApiService(){
        return apiService;
    }

}

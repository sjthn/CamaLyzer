package app.camalyzer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    public void getApiResponse() {
        ApiService apiService = new RestClient().getApiService();
        apiService.getServiceResponse("", new RestCallback<RestError>() {
            @Override
            public void success(RestError restError, Response response) {

                Toast.makeText(getBaseContext(), "Response " + response.getStatus(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(RestError restError) {
                Toast.makeText(getBaseContext(), "Response " + restError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface ApiService {

        @GET("/image_responses/[token]")
        public void getServiceResponse(@Path("token") String token, RestCallback<RestError> callback);

        @POST("/image_requests")
        public void postServiceRequest();

    }

}

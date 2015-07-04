package app.camalyzer.app;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import app.camalyzer.R;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private Camera.Parameters params;
    private Button mCaptureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera);
        surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mCaptureBtn = (Button) findViewById(R.id.capture_btn);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        //surfaceHolder.setFixedSize(176, 144);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success)
                            mCamera.takePicture(shutterlistener(), null, picturelistener());
                    }
                });
            }
        });
    }

    private Camera.ShutterCallback shutterlistener() {

        return new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };

    }

    private Camera.PictureCallback picturelistener() {

        return new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

            }
        };

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
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = Camera.open();

        params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }


        mCamera.setParameters(params);
        mCamera.startPreview();
        mCamera.setDisplayOrientation(90);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            mCamera.stopPreview();
        }

    }

    public interface ApiService {

        @GET("/image_responses/[token]")
        public void getServiceResponse(@Path("token") String token, RestCallback<RestError> callback);

        @POST("/image_requests")
        public void postServiceRequest();

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

}

package app.camalyzer.app;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import app.camalyzer.R;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

@SuppressWarnings({"ALL", "deprecation"})
public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private static final String locale = "en-US";

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private Camera.Parameters params;
    private Button mCaptureBtn;

    private static Camera.Size smallestSize;

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
                findViewById(R.id.preview_small_fl).setVisibility(View.VISIBLE);
                ImageView previewImage = (ImageView) findViewById(R.id.focus_rectangle);
                previewImage.setImageBitmap(generateBitmapImage(data));
                postApiRequest(getImagePath());

                /*Bitmap scaled = Bitmap.createScaledBitmap(generateBitmapImage(data), smallestSize.height, smallestSize.width, true);
                int w = scaled.getWidth();
                int h = scaled.getHeight();
                // Setting post rotate to 90
                Matrix mtx = new Matrix();
                mtx.postRotate(90);
                // Rotating Bitmap
                Bitmap bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                previewImage.setImageBitmap(bm);*/

            }
        };

    }

    private String getImagePath() {

        String selectedImagePath = null;
        Uri selectedImageUri = getTempUri();
        Cursor cursor = getContentResolver().query(
                selectedImageUri, null, null, null, null);
        if (cursor == null) {
            selectedImagePath = selectedImageUri.getPath();
        }

        return selectedImagePath;

    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {

        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/CamaLyzer Backup");
        if (!dir.exists())
            dir.mkdirs();

        File imgFile = new File(dir + "/CamaLyzer_img.jpeg");

        if (imgFile.exists()) {
            imgFile.delete();
            imgFile = new File(dir + "/CamaLyzer_img.jpeg"); //
        }

        return imgFile;

    }

    public void postApiRequest(String img_path) {
        /*ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        TypedInput in = new TypedByteArray("application/unknown", ba);*/
        ApiService apiService = new RestClient().getApiService();
        apiService.postServiceRequest(img_path, locale, new RestCallback<RestError>() {
            @Override
            public void success(RestError restError, Response response) {
                Toast.makeText(getBaseContext(), "Response: " +
                                response.getStatus() + ", TOKEN: " + restError.getToken(),
                        Toast.LENGTH_LONG).show();
                getApiResponse(restError.getToken());
            }

            @Override
            public void failure(RestError restError) {
                Toast.makeText(getBaseContext(), "RestError: " + restError.mErrStatus, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getApiResponse(String token) {
        ApiService apiService = new RestClient().getApiService();
        apiService.getServiceResponse(token, new RestCallback<RestError>() {
            @Override
            public void success(RestError restError, Response response) {

                Toast.makeText(getBaseContext(), "Response " + response.getStatus(), Toast.LENGTH_LONG).show();
                TextView recognized_txtview = (TextView) findViewById(R.id.recognized_txtview);
                recognized_txtview.setText(restError.getName());

            }

            @Override
            public void failure(RestError restError) {
                Toast.makeText(getBaseContext(), "Response " + restError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private static Bitmap generateBitmapImage(byte[] data) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateSampleSize(options, smallestSize.width, smallestSize.height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        //Toast.makeText(getBaseContext(),
        //        "Res: " + options.outWidth + " , " + options.outHeight
        //               + " , " + options.outMimeType, Toast.LENGTH_LONG).show();

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);

    }

    private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = Camera.open();

        params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes,
                surfaceView.getWidth(),
                surfaceView.getHeight());

        params.setPreviewSize(optimalSize.width, optimalSize.height);
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
            mCamera.release();
            mCamera = null;
        }

    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.01;
        double targetRatio = (double) w / h;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        smallestSize = null;

        // Find size
        for (Camera.Size size : sizes) {
            Log.e("VideoFragment", "Support SIzes =======>>> " + size.width + " , " + size.height);

            if (smallestSize == null) {
                smallestSize = size;
            }
            if ((size.width < smallestSize.width) && (size.height < smallestSize.height)) {
                smallestSize = size;
            }

            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
            Log.e("VideoFragment", "Optimal SIze 1 -------->" + optimalSize.width + " , " + optimalSize.height);
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
            Log.e("CameraActivity", "Optimal SIze 2 -------->" + optimalSize.width + " , " + optimalSize.height);
        }
        return optimalSize;
    }

    public interface ApiService {

        @Headers("Authorization: CloudSight [Key]")
        @GET("/image_responses/{token}")
        public void getServiceResponse(@Path("token") String token, RestCallback<RestError> callback);

        @Multipart
        @Headers("Authorization: CloudSight [Key]")
        @POST("/image_requests")
        public void postServiceRequest(@Part("image_request[remote_image_url]") String image, @Part("image_request[locale]") String locale, RestCallback<RestError> restCallback);
        //public void postServiceRequest(@Part("image_request[image]") TypedInput image, @Part("image_request[locale]") String locale, RestCallback<RestError> restCallback);

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

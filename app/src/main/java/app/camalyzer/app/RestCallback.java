package app.camalyzer.app;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by admin on 7/3/2015.
 */
public abstract class RestCallback<T> implements Callback<T> {

    public abstract void failure(RestError restError);

    /*@Override
    public void success(T t, Response response) {

    }*/

    @Override
    public void failure(RetrofitError error) {
        RestError restError = (RestError) error.getBodyAs(RestError.class);

        if (restError != null)
            failure(restError);
        else {
            failure(new RestError(error.getMessage()));
        }
    }
}

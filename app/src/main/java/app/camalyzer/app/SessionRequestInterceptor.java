package app.camalyzer.app;

import retrofit.RequestInterceptor;

/**
 * Created by admin on 7/3/2015.
 */
public class SessionRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        if (true) {
            request.addHeader("Header name", "Header value");
        }
    }
}

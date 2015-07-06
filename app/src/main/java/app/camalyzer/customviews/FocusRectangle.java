package app.camalyzer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 7/4/2015.
 */
public class FocusRectangle extends View {

    private static final String TAG = "FocusRectangle";

    public FocusRectangle(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    private void setDrawable(int paramInt) {
        setBackgroundDrawable(getResources().getDrawable(paramInt));
    }

    public void clear() {
        setBackgroundDrawable(null);
    }

    public void showFail() {
        setDrawable(2130837560);
    }

    public void showStart() {
        setDrawable(2130837562);
    }

    public void showSuccess() {
        setDrawable(2130837561);
    }

}

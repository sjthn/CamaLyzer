package app.camalyzer.customviews;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import app.camalyzer.R;
import app.camalyzer.constants.Constants;

/**
 * Created by admin on 7/4/2015.
 */
public class PreviewFrameLayout extends ViewGroup {

    private FrameLayout mFrame;
    private Camera.Size mPreviewSize;
    private int width, height;

    private double mAspectRatio = 1.333333333333333D;

    private OnSizeChangedListener mSizeListener;

    public PreviewFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //this.mFrame = (FrameLayout) findViewById(R.id.frame);
        if (this.mFrame == null) {
            throw new IllegalStateException("must provide child with id as \"frame\"");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        /*int diffwidth, diffheight, k, m, l;

        label2:
        if ((changed) && (getChildCount() > 0)) {

            diffwidth = right - left;
            diffheight = bottom - top;
            k = diffwidth;
            m = diffheight;

            label:
            if (this.mPreviewSize != null) {

                if ((Constants.SCREENORIENTATION != 90) && (Constants.SCREENORIENTATION != 270)) {
                    // break label221;
                }
                k = this.mPreviewSize.height;
                m = this.mPreviewSize.width;

            }
            Log.e("rvg", "preview Width " + k);
            Log.e("rvg", "preview Height " + m);
            int n = k * diffheight / m;
            l = m * diffwidth / k;
            measureChild(this.mFrame,
                    View.MeasureSpec.makeMeasureSpec(n, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(l, MeasureSpec.EXACTLY));
            if (diffwidth * m <= diffheight * k) {
                //break label242;
            }
            this.mFrame.layout((diffwidth - n) / 2, 0, (diffwidth + n) / 2, diffheight);

        }*/

        /*for (; ; ) {
            if (this.mSizeListener != null) {
                this.mSizeListener.onSizeChanged();
            }
            return;
            //label221:
            k = this.mPreviewSize.width;
            m = this.mPreviewSize.height;
            break;
            //label242:
            Log.d("rvg ", "(height - scaledChildHeight) / 2 " + (diffheight - 1) / 2);
            Log.d("rvg ", "width " + diffwidth);
            Log.d("rvg ", "(height + scaledChildHeight) / 2) " + (diffheight + 1) / 2);
            this.mFrame.layout(0, (diffheight - 1) / 2, diffwidth, (diffheight + 1) / 2);
        }*/

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("rvg", "onMeasure width " + this.width);
        Log.d("rvg", "onMeasure height " + this.height);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case event.ACTION_OUTSIDE:
                break;

        }

        return super.onTouchEvent(event);
    }*/

    public void setAspectRatio(double paramDouble) {
        if (paramDouble <= 0.0D) {
            throw new IllegalArgumentException();
        }
        if (this.mAspectRatio != paramDouble) {
            this.mAspectRatio = paramDouble;
            requestLayout();
        }
    }

    public void setHeight(int paramInt) {
        this.height = paramInt;
    }

    public void setOnSizeChangedListener(OnSizeChangedListener paramOnSizeChangedListener) {
        this.mSizeListener = paramOnSizeChangedListener;
    }

    public void setPreviewSize(Camera.Size paramSize) {
        this.mPreviewSize = paramSize;
    }

    public void setWidth(int paramInt) {
        this.width = paramInt;
    }

    public static abstract interface OnSizeChangedListener {
        public abstract void onSizeChanged();
    }

}

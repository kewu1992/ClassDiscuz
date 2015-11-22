package cmu.banana.classdiscuz.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by WK on 11/22/15.
 *
 * reference: http://stackoverflow.com/questions/8232608/fit-image-into-imageview-keep-aspect-ratio-and-then-resize-imageview-to-image-d
 */
public class BitmapScale {
    public static Bitmap bitmapScale(Context context, Bitmap bitmap, int boundDp){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bound = dpToPx(boundDp, context);

        float xScale = ((float) bound) / width;
        float yScale = ((float) bound) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private static int dpToPx(int dp, Context context)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}

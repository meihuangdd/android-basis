package simcpux.sourceforge.net.color_select.util;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.nio.ByteBuffer;

public class ColorUtil {
    static final String TAG = "ColorUtil";
    public static ImageReader mImageReader;
    static Bitmap bitmap;

    /**
     * 坐标
     * @param x
     * @param y
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int getColor(int x, int y){
        if(mImageReader == null){
            Log.w(TAG, "getColor: mImageReader is null" );
            return -1;
        }
        Image image = mImageReader.acquireLatestImage();
        if(image == null){
            if (bitmap == null) {
                Log.w(TAG, "getColor: bitmap is null");
                return -1;
            }
            return bitmap.getPixel(x,y);//指定坐标的颜色
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        }
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        return bitmap.getPixel(x, y);
    }
}

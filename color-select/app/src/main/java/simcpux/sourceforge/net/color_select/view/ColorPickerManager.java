package simcpux.sourceforge.net.color_select.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import simcpux.sourceforge.net.color_select.util.ColorUtil;

public class ColorPickerManager {

    private static WindowManager windowManager;

    private MediaProjectionManager mMediaProjectionManager;
    private static MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private int REQUEST_MEDIA_PROJECTION =1;
    private Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerManager(WindowManager windowManager, Activity activity){
        this.windowManager = windowManager;
        this.activity = activity;
        mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        assert mMediaProjectionManager != null;
        mMediaProjection = mMediaProjectionManager.getMediaProjection(REQUEST_MEDIA_PROJECTION,  mMediaProjectionManager.createScreenCaptureIntent());
        setUpVirtualDisplay();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setUpVirtualDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(dm);
        ImageReader imageReader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, PixelFormat.RGBA_8888, 1);
        mMediaProjection.createVirtualDisplay("ScreenCapture",dm.widthPixels, dm.heightPixels, dm.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
        ColorUtil.mImageReader = imageReader;
    }
}

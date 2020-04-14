package simcpux.sourceforge.net.color_select.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.math.RoundingMode;

import simcpux.sourceforge.net.color_select.R;
import simcpux.sourceforge.net.color_select.util.ColorUtil;

/**
 * @author meihuang
 * @1739783580@qq.com
 */
public class ColorPickerView extends View {
    public static WindowManager windowManager;
    public static Activity activity;

    private  final  static String TAG = "ColorPickerView";
    private static final int[] GRAD_COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
    private Paint mPaint;//主颜色选择圈
    private int mRadius = 400;
    private int mStrokeWidth = 90;
    private int mColor = 0;
    private Bitmap mBackgroundBitmap;


    private Paint mPaintIndicators;//颜色选择圈的指标
    private float indicatorsX = 100;        //颜色选择圈默认X坐标
    private float indicatorsY = 100;        //颜色选择圈默认Y坐标
    private float indicatorsX1 = 0;
    private float indicatorsY1 = 0;
    private int indicatorsRadius = 50;  //颜色选择圈默认半径大小
    private int smallRoundRadius = 0;  //小圆活动半径



    private Paint mPaintBrightness;//亮度选择圈
    private int mRadiusBrightness = 250;
    private Paint mPaintBrightnessIndicators;//亮度选择圈的指标
    private float brightnessIndicatorsX = 50;        //亮度选择圈默认X坐标
    private float brightnessIndicatorsY = 50;        //亮度选择圈默认Y坐标
    private float brightnessIndicatorsX1 = 0;
    private float brightnessIndicatorsY1 = 0;
    private int brightnessIndicatorsRadius = 20;  //亮度选择圈默认半径大小

//    private  ColorPickerManager colorPickerManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerView(Context context) {
        super(context);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        setClickable(true);

//        size = Math.min(mBackgroundBitmap.getWidth(),mBackgroundBitmap.getHeight());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintIndicators = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaintBrightness = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBrightnessIndicators = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, isInEditMode() ? null : mPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: changed"+changed+"left"+left+"top"+top+"rigth"+right+"bottom"+bottom);
        indicatorsX = getWidth()/2;
        indicatorsY = getHeight()/2-mRadius;
        brightnessIndicatorsX = getWidth()/2;
        brightnessIndicatorsY = getHeight()/2-mRadiusBrightness;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds();
        buildShader(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
//                mColor = mBackgroundBitmap.getPixel((int) event.getX(), (int)event.getY());
                setWHs(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
//                mColor = mBackgroundBitmap.getPixel((int) event.getX(), (int)event.getY());
                setWHs(event);

                break;
            case MotionEvent.ACTION_UP:
                //松开
//                mColor = mBackgroundBitmap.getPixel((int) event.getX(), (int)event.getY());
                setWHs(event);

                break;
        }
//        return super.onTouchEvent(event);(有可能返回false)
        Log.i(TAG, "onTouchEvent: event.getX()="+event.getX()+"event.getY()="+event.getY());
        return true;
    }

    private void setWHs(MotionEvent event){
        smallRoundRadius = mRadius - mStrokeWidth; //小圆活动半径 = 大圆活动半径 - 大圆的变宽
        if( event.getX() > getWidth()/2 + smallRoundRadius-50 ||
                event.getX() < getWidth()/2 - smallRoundRadius+50 ||
                event.getY() >getHeight()/2 + smallRoundRadius-50 ||
                event.getY() <getHeight()/2 - smallRoundRadius +50){
            setWH(event.getX(),event.getY(),"big");
        }else{
            setWH(event.getX(),event.getY(),"small");
        }
    }
    /**
     * 设置当前相应触摸圆的坐标
     * @param getX 当前触摸的宽
     * @param getY 当前触摸的高
     * @param type 当前是大圆还是小圆
     */
    private void setWH(float getX,float getY,String type){
        if(type.equals("big")){
            indicatorsX1 = getX;
            indicatorsY1 = getY;
        }else if(type.equals("small")){
            brightnessIndicatorsX1 = getX;
            brightnessIndicatorsY1 = getY;
        }
        // 通知重绘
        postInvalidate();    //该方法会调用onDraw方法，重新绘图
    }
    /**
     * Shader(绘制色谱)
     *
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void buildShader(Canvas canvas) {
        OuterRing(canvas);
        InnerRing(canvas);
    }

    /**
     * 绘制外圈
     *
     * @param canvas
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void OuterRing(Canvas canvas) {

        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, GRAD_COLORS, null);
        mPaint.setShader(sweepGradient);
        mPaint.setStyle(Paint.Style.STROKE);//设置为空心圆
        mPaint.setStrokeWidth(mStrokeWidth);//宽度为100
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
        mPaintIndicators.setColor(ColorUtil.getColor((int)indicatorsX,(int) indicatorsY));
        mPaintIndicators.setStyle(Paint.Style.FILL);//设置为空心圆
        ReviseIndicators();//修正
        canvas.drawCircle(indicatorsX, indicatorsY, indicatorsRadius, mPaintIndicators);


    }

    /**
     * 修正外圈小的坐标
     */
    private void ReviseIndicators(){
        Double  newAngle = Math.atan((indicatorsY1-getHeight() / 2)/(indicatorsX1-getWidth() / 2));//计算弧度
        if(indicatorsX1-getWidth() / 2 <0){
            newAngle= newAngle +Math.PI;
        }
        indicatorsX = (float) (Math.cos(newAngle)*mRadius)+getWidth() / 2;
        indicatorsY = (float) (Math.sin(newAngle)*mRadius)+getHeight() / 2;
        Log.i(TAG, "ReviseIndicators: newAngle="+newAngle+"indicatorsX="+indicatorsX1+"indicatorsY="+indicatorsY1);
    }
    /**
     * 绘制内圈
     *
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void InnerRing(Canvas canvas) {
        SweepGradient sweepGradientBrightness = new SweepGradient(getWidth() / 3, getHeight() / 3, new int[]{Color.YELLOW, Color.YELLOW}, null);
        mPaintBrightness.setShader(sweepGradientBrightness);
        mPaintBrightness.setStyle(Paint.Style.STROKE);//设置为空心圆
        mPaintBrightness.setStrokeWidth(70);//宽度为100
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadiusBrightness, mPaintBrightness);
        mPaintBrightnessIndicators.setColor(Color.RED);
        mPaintBrightnessIndicators.setStyle(Paint.Style.FILL);//设置为空心圆
        canvas.drawCircle(brightnessIndicatorsX, brightnessIndicatorsY, brightnessIndicatorsRadius, mPaintBrightnessIndicators);
        ReviseBrightnessIndicators();//修正
    }
    /**
     * 修正内圈小的坐标
     */
    private void ReviseBrightnessIndicators(){
        Double  newAngle = Math.atan((brightnessIndicatorsY1-getHeight() / 2)/(brightnessIndicatorsX1-getWidth() / 2));//计算弧度
        if(brightnessIndicatorsX1-getWidth() / 2 <0){
            newAngle= newAngle +Math.PI;
        }
        brightnessIndicatorsX = (float) (Math.cos(newAngle)*mRadiusBrightness)+getWidth() /2;
        brightnessIndicatorsY = (float) (Math.sin(newAngle)*mRadiusBrightness)+getHeight() /2;
        Log.i(TAG, "ReviseIndicators: newAngle="+newAngle+"indicatorsX="+brightnessIndicatorsX1+"indicatorsY="+brightnessIndicatorsY1);
    }

}

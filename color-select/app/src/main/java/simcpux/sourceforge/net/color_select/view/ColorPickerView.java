package simcpux.sourceforge.net.color_select.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author meihuang
 * @1739783580@qq.com
 */
public class ColorPickerView extends View {
    private static final int[] GRAD_COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
    private Paint mPaint;//主颜色选择圈
    private Paint mPaintIndicators;//颜色选择圈的指标
    private float indicatorsX = 100;        //颜色选择圈默认X坐标
    private float indicatorsY = 100;        //颜色选择圈默认Y坐标
    private int indicatorsRadius = 50;  //颜色选择圈默认圆角大小
    private Paint mPaintBrightness;//亮度选择圈
    private Paint mPaintBrightnessIndicators;//亮度选择圈的指标
    private float brightnessIndicatorsX = 50;        //亮度选择圈默认X坐标
    private float brightnessIndicatorsY = 50;        //亮度选择圈默认Y坐标
    private int brightnessIndicatorsRadius = 20;  //亮度选择圈默认圆角大小

    public ColorPickerView(Context context) {
        super(context);
        init();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        setClickable(true);
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        buildShader(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                indicatorsX = (int) event.getX();
                indicatorsY = (int) event.getY();
                // 通知重绘
                postInvalidate();    //该方法会调用onDraw方法，重新绘图
                break;
            case MotionEvent.ACTION_MOVE:
                indicatorsX = (int) event.getX();
                indicatorsY = (int) event.getY();
                // 通知重绘
                postInvalidate();    //该方法会调用onDraw方法，重新绘图
                //移动
                break;
            case MotionEvent.ACTION_UP:
                indicatorsX = (int) event.getX();
                indicatorsY = (int) event.getY();
                // 通知重绘
                postInvalidate();    //该方法会调用onDraw方法，重新绘图
                //松开
                break;
        }

//        return super.onTouchEvent(event);(有可能返回false)

        return true;
    }

    /**
     * Shader(绘制色谱)
     *
     * @param canvas
     */
    private void buildShader(Canvas canvas) {
        OuterRing(canvas);
        InnerRing(canvas);
    }

    /**
     * 绘制外圈
     *
     * @param canvas
     */
    private void OuterRing(Canvas canvas) {
        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, GRAD_COLORS, null);
        mPaint.setShader(sweepGradient);
        mPaint.setStyle(Paint.Style.STROKE);//设置为空心圆
        mPaint.setStrokeWidth(90);//宽度为100
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 400, mPaint);
        mPaintIndicators.setColor(Color.RED);
        mPaintIndicators.setStyle(Paint.Style.FILL);//设置为空心圆
        canvas.drawCircle(indicatorsX, indicatorsY, indicatorsRadius, mPaintIndicators);
        ReviseIndicators();//修正

    }

    /**
     * 修正外圈小的坐标
     */
    private void ReviseIndicators(){
        if (indicatorsX <= indicatorsRadius) {
            indicatorsX = indicatorsRadius;
        } else if (indicatorsX >= (getWidth() - indicatorsRadius)) {
            indicatorsX = getWidth() - indicatorsRadius;
        }
        if (indicatorsY <= indicatorsRadius) {
            indicatorsY = indicatorsRadius;
        } else if (indicatorsY >= (getHeight() - indicatorsRadius)) {
            indicatorsY = getHeight() - indicatorsRadius;
        }
    }
    /**
     * 绘制内圈
     *
     * @param canvas
     */
    private void InnerRing(Canvas canvas) {
        SweepGradient sweepGradientBrightness = new SweepGradient(getWidth() / 3, getHeight() / 3, new int[]{Color.YELLOW, Color.YELLOW}, null);
        mPaintBrightness.setShader(sweepGradientBrightness);
        mPaintBrightness.setStyle(Paint.Style.STROKE);//设置为空心圆
        mPaintBrightness.setStrokeWidth(70);//宽度为100
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 250, mPaintBrightness);
        ReviseBrightnessIndicators();//TODO:修正
    }
    /**
     * 修正内圈小的坐标
     */
    private void ReviseBrightnessIndicators(){

    }
}

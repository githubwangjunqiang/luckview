package com.haowu.onionvideo.luck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${王俊强} on 2018/9/3.
 */
public class luckView extends SurfaceView implements SurfaceHolder.Callback, Runnable, IluckView {
    private int SIZE = 100;
    private boolean isRunning;
    private float startAngle;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Path mPath;
    private volatile List<LuckData> mLuckDatas;
    private float mRanius;
    private RectF mRectF;
    private int mSpeed = 0;
    private int luckIndex = -1;
    private float startIndexAngle, stopIndexAngle;

    public luckView(Context context) {
        this(context, null);
    }

    public luckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public luckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                getContext().getResources().getDisplayMetrics()));
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        SIZE = dp2px(getContext(), 100);
        mLuckDatas = new ArrayList<>();
        mRectF = new RectF();
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(SIZE, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = Math.max(getPaddingLeft(), dp2px(getContext(), 6)) * 2;
        mRanius = (w - padding) / 2;
        mRectF.set(padding, padding, w - padding,
                h - padding);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        startThread();
    }

    private void startThread() {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            drawLuck();



            long endTime = System.currentTimeMillis();
            if (endTime - startTime < 50) {
                try {
                    Thread.sleep(50 - (endTime - startTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawLuck() {
        if (mLuckDatas == null || mLuckDatas.isEmpty()) {
            return;
        }
        Canvas canvas = null;
        try {
            canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                drawLuckViews(canvas);
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawLuckViews(Canvas canvas) {
        float start = startAngle;
        float sweep = 360 / mLuckDatas.size();
        canvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRanius, mPaint);
        for (int i = 0; i < mLuckDatas.size(); i++) {
            LuckData luckData = mLuckDatas.get(i);
            mPaint.setColor(luckData.getBackColor());
            canvas.drawArc(mRectF, start, sweep, true, mPaint);
            mPath.reset();
            mPath.addArc(mRectF, start, sweep);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float v = Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.leading) + Math.abs(fontMetrics.descent);
            mPaint.setColor(Color.WHITE);
            canvas.drawTextOnPath(luckData.getName(), mPath, 0, v, mPaint);
            start += sweep;
        }
        float index = 0;
        float endAngle = startAngle;
        if (endAngle >= 360) {
            index = endAngle % 360;
        } else {
            index = endAngle;
        }


        startAngle += mSpeed;

        if (luckIndex >= 0) {
            mSpeed--;
            if (mSpeed == 5) {
                if (index < stopIndexAngle && index > startIndexAngle) {
                    mSpeed = 0;
                } else {
                    mSpeed += 1;
                }
            }
        }

        if (mSpeed <= 0) {
            mSpeed = 0;
            stop();
        }
    }

    private void stop() {
        luckIndex = -1;
    }

    @Override
    public void addLuckData(LuckData data) {
        if (data != null && mLuckDatas != null) {
            mLuckDatas.add(data);
        }
    }

    @Override
    public void addLuckDatas(List<LuckData> datas) {
        if (datas != null && mLuckDatas != null) {
            mLuckDatas.addAll(datas);
        }
    }

    @Override
    public void startLuck() {
        mSpeed = 50;
    }

    @Override
    public void stopLuck(LuckData data) {
        for (int i = 0; i < mLuckDatas.size(); i++) {
            if (mLuckDatas.get(i).getId() == data.getId()) {
                luckIndex = i;
                stopIndexAngle = 270 - 360 / mLuckDatas.size() * i;
                startIndexAngle = stopIndexAngle - 360 / mLuckDatas.size();
                return;
            }
        }
    }

    @Override
    public boolean isTruning() {
        return mSpeed != 0;
    }
}

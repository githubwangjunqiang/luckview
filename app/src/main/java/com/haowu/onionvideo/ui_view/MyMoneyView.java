package com.haowu.onionvideo.ui_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.haowu.onionvideo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by ${王俊强} on 2018/4/21.
 */

public class MyMoneyView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "12345";

    public MyMoneyView(Context context) {
        super(context);
        init();
    }

    public MyMoneyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyMoneyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private List<PointF> mRectFList;
    private List<PathMeasure> mPathMeasures;
    private List<Bitmap> mIntegers;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private float[] mCurrentPosition = new float[2];
    private int bitmapHeight, bitmapWidtg;
    private int count = 16;

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mRectFList = new ArrayList<>();
        mPathMeasures = new ArrayList<>();
        mIntegers = new ArrayList<>();
        initBitmapID();
        initRectf();
        bitmapHeight = mIntegers.get(0).getHeight();
        bitmapWidtg = mIntegers.get(0).getWidth();
    }

    private void initBitmapID() {
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb1));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb2));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb3));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb4));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb5));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb1));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb2));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb3));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb4));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb5));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb1));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb2));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb3));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb4));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb5));
        mIntegers.add(BitmapFactory.decodeResource(getResources(), R.mipmap.jb1));
    }

    private void initRectf() {
        for (int i = 0; i < count; i++) {
            mRectFList.add(new PointF(1, 1));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        h = h - 20;
        for (int i = 0; i < mRectFList.size(); i++) {
            mRectFList.get(i).set(w / 2, h);
        }
        initPath(w, h);
        mValueAnimator = initAnimation(w, h);

    }

    /**
     * 初始化 路径
     *
     * @param w
     * @param h
     */
    private void initPath(int w, int h) {
        int weight = w / 3;
        Path path = new Path();
        path.moveTo(w / 2, h);
        float kX = w / 2;
        int kY = -h / 2 * 4 / 3;
        float endX = w / 2;
        path.quadTo(kX,
                kY,
                endX, h);
        mPathMeasures.add(new PathMeasure(path, false));
        int[] posw = new int[count];
        for (int i = 0; i < count; i++) {
            posw[i] = weight / count * i + weight;
        }
        int[] posend = new int[count];
        for (int i = 0; i < count; i++) {
            posend[i] = w / 2 / count * i + w / 2 / 2;
        }

        for (int i = 0; i < count - 1; i++) {
            Path pathI = new Path();
            pathI.moveTo(w / 2, h);
            int index = new Random().nextInt(count);
            float kiX = posw[index];
            int kiY = -new Random().nextInt((h / 2));
//            float endiX = new Random().nextInt(w - bitmapWidtg * 2) + bitmapWidtg;
            float endiX = posend[index];
            pathI.quadTo(kiX,
                    kiY,
                    endiX, h);
            mPathMeasures.add(new PathMeasure(pathI, false));

        }
    }

    /**
     * 获取动画
     *
     * @param w
     * @param h
     * @return
     */
    private ValueAnimator initAnimation(int w, int h) {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0f, mPathMeasures.get(0).getLength());
            mValueAnimator.setDuration(1500);
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    mPathMeasures.get(0).getPosTan(animatedValue, mCurrentPosition, null);
                    float pathSize = animatedValue / (mPathMeasures.get(0).getLength() / 100);
                    setAlpha(1 - pathSize / 100);
                    for (int i = 0; i < mPathMeasures.size(); i++) {

                        float[] currentPos = new float[2];
                        mPathMeasures.get(i).getPosTan(animatedValue, currentPos, null);

                        mRectFList.get(i).set(currentPos[0], currentPos[1]);

                    }
                    postInvalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    for (int i = 0; i < mRectFList.size(); i++) {
                        mRectFList.get(i).set(w / 2, h);
                    }
                    postInvalidate();
                }
            });
        }
        return mValueAnimator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRectFList.size(); i++) {
            canvas.drawBitmap(mIntegers.get(i), mRectFList.get(i).x, mRectFList.get(i).y - bitmapHeight, mPaint);
        }
    }

    public void startTranAnimation() {
        if (mValueAnimator != null) {
            if (mValueAnimator.isRunning()) {
                mValueAnimator.end();
            }
            mValueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIntegers.clear();
        mRectFList.clear();
        mPathMeasures.clear();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }

    }
}

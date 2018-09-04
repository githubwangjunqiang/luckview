package com.haowu.onionvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haowu.onionvideo.ui_view.BezierEvaluator;
import com.haowu.onionvideo.ui_view.MyMoneyView;
import com.haowu.onionvideo.ui_view.ShoppingCartAnim;
import com.xiaoqiang.zhujing.luckview.IluckView;
import com.xiaoqiang.zhujing.luckview.LuckData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "12345";
    private MyMoneyView mMyMoneyView;
    private LinearLayout mFrameLayout;
    private ImageView mImageViewStart;
    private ShoppingCartAnim cartAnim;
    private IluckView mIluckView;
    private LinearLayout mLinearLayout;
    private List<LuckData> mLuckData = new ArrayList<>();
    private LuckData mData;
    private TextView mTextViewName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyMoneyView = findViewById(R.id.money_view);
        mFrameLayout = findViewById(R.id.layout);
        mImageViewStart = findViewById(R.id.viewStart);
        mTextViewName = findViewById(R.id.tvname);
        mIluckView = findViewById(R.id.luckview);
        mLinearLayout = findViewById(R.id.layout_lin);
        View view = findViewById(R.id.ivimage);
        view.post(new Runnable() {
            @Override
            public void run() {
              view.setTranslationY(-view.getHeight()/4+8);
            }
        });
        TextView viewById = findViewById(R.id.tv);
        CharSequence dd = getStrings();
        viewById.setText(dd.toString());
        cartAnim = new ShoppingCartAnim(this, R.mipmap.ic_launcher, null);

//        mImageViewStart.setOnClickListener(v -> cartAnim.startAnim(mImageViewStart, mImageViewEnd, null));


        mMyMoneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyMoneyView.startTranAnimation();
            }
        });
        mImageViewStart.post(() -> {
            View views = getView(mImageViewStart);
            mImageViewStart.setOnClickListener(v -> inanimation(views));
        });

        setData();
    }

    public CharSequence getStrings() {
        return "世界\n你好";
    }

    private View getView(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        Log.d(TAG, "getView: " + location[0] + "---" + location[1]);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = location[0];
        layoutParams.topMargin = (int) v.getY();
        imageView.setLayoutParams(layoutParams);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        decorView.addView(imageView);
        imageView.setVisibility(View.GONE);
        return imageView;
    }

    private void inanimation(View view) {
        BezierEvaluator bezierEvaluator = new BezierEvaluator(
                new PointF(view.getX() - view.getWidth(), 0f));
        PointF startPointF = new PointF(view.getX(), view.getY());
        float v = new Random((long) view.getX()).nextFloat() + 20;
        PointF endPointF = new PointF(v, view.getY());
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, startPointF, endPointF);
        anim.setDuration(400);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: " + point.toString());
                view.setTranslationY(point.y);
                view.setTranslationX(point.x);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setTranslationY(startPointF.y);
                view.setTranslationX(startPointF.x);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    private void setData() {
        for (int i = 0; i < 2; i++) {
            LuckData data = new LuckData("中大奖了" + i, 0, 0,i % 2 == 0 ? Color.BLUE : Color.GREEN,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), 1 + i);
            mLuckData.add(data);

            TextView tvView = new TextView(this);
            tvView.setPadding(20, 20, 20, 20);
            tvView.setGravity(Gravity.CENTER);
            tvView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 12, getResources().getDisplayMetrics()));
            tvView.setText("停在\n" + i + "\n位置");
            tvView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData = data;
                    mTextViewName.setText(data.getName());
                }
            });
            mLinearLayout.addView(tvView);
        }
        mIluckView.addLuckDatas(mLuckData);
    }

    public void doClick(View view) {
        if (mIluckView.isTruning()) {
            mIluckView.stopLuck(mData == null ? mLuckData.get(0) : mData);
        } else {
            mIluckView.startLuck();
        }
    }
}

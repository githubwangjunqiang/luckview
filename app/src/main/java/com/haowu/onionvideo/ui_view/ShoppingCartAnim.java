package com.haowu.onionvideo.ui_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by ${王俊强} on 2018/4/23.
 */

public class ShoppingCartAnim {
    private ImageView buyImg;//播放动画的参照imageview
    private int[] start_location = new int[2];// 这是用来存储动画开始位置的X、Y坐标;
    private int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标;
    public ViewGroup root;//动画层

    private interface CallBack {
        void onAnimationStart();

        void onAnimationRepeat();

        void onAnimationEnd();
    }


    public ShoppingCartAnim(Activity activity, int drawableID, Bitmap bitmap) {
        buyImg = new ImageView(activity);//buyImg是动画的图片
        if (bitmap == null) {
            buyImg.setImageResource(drawableID);// 设置buyImg的图片
        } else {
            buyImg.setImageBitmap(bitmap);//也可以设置bitmap，可以用商品缩略图来播放动画
        }
        root = (ViewGroup) activity.getWindow().getDecorView();//创建一个动画层
        root.addView(buyImg);//将动画参照imageview放入
        buyImg.setVisibility(View.GONE);
    }

    /**
     * 将image图片添加到动画层并放在起始坐标位置
     *
     * @param view     播放动画的view
     * @param location 起始位置
     * @return
     */
    private View addViewFromAnimLayout(View view, int[] location) {
        int x = location[0];
        int y = location[1];
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 开始动画
     *
     * @param startView
     * @param endView
     */
    public void startAnim(View startView, View endView, CallBack callBack) {
        // 这是获取起始目标view在屏幕的X、Y坐标（这也是动画开始的坐标）
        startView.getLocationInWindow(start_location);
        // 购物车结束位置
        endView.getLocationInWindow(end_location);
        //将动画图片和起始坐标绘制成新的view，用于播放动画
        //将image图片添加到动画层
        /**这里为什么不直接传一个图片而是传一个imageview呢？
         * 因为我这样做的目的是clone动画播放控件，为什么要clone呢？
         * 因为如果用户连续点击添加购物车的话，如果只用一个imageview去播放动画的话，这个动画就会成还没播放完就回到原点重新播放。
         * 而如果clone一个imageview去播放，那么这个动画还没播放完，用户再点击添加购物车以后我们还是clone一个新的imageview去播放。
         * 这样动画就会出现好几个点而不是一个点还没播放完又缩回去。
         * 说的通俗点，就是依靠这个方法，把参照对象和起始位置穿进去，得到一个clone的对象来播放动画
         */View run_view = addViewFromAnimLayout(buyImg, start_location);

        // 计算位移
        int endX = end_location[0] - start_location[0];
        int endY = end_location[1] - start_location[1];

        //平移动画 绘制X轴 0到结束的x轴
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        //设置线性插值器
        translateAnimationX.setInterpolator(new LinearInterpolator());
        // 动画重复执行的次数
        translateAnimationX.setRepeatCount(0);
        //设置动画播放完以后消失，终止填充
        translateAnimationX.setFillAfter(true);

        //平移动画 绘制Y轴
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);
        //将两个动画放在动画播放集合里
        // 设置false使每个子动画都使用自己的插值器
        AnimationSet set = new AnimationSet(false);

        //设置动画播放完以后消失，终止填充
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(400);// 动画的执行时间
        /**
         * 动画开始播放的时候，参照对象要显示出来，如果不显示的话这个动画会看不到任何东西。
         * 因为不管用户点击几次动画，播放的imageview都是从参照对象buyImg中clone来的
         * */
        buyImg.setVisibility(View.VISIBLE);
        run_view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                if (callBack != null) {
                    callBack.onAnimationStart();
                }

            }

            //动画重复中
            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
                if (callBack != null) {
                    callBack.onAnimationRepeat();
                }
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //动画播放完以后，参照对象要隐藏
                buyImg.setVisibility(View.GONE);
                //结束后访问数据
                if (callBack != null) {
                    callBack.onAnimationEnd();
                }
            }
        });
    }

    public static class Listener implements CallBack {

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationRepeat() {

        }

        @Override
        public void onAnimationEnd() {

        }
    }
}

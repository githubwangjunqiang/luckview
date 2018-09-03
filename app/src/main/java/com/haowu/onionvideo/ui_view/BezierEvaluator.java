package com.haowu.onionvideo.ui_view;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by ${王俊强} on 2017/12/17.
 */

public class BezierEvaluator implements TypeEvaluator<PointF> {
    private PointF mControlPoint;

    public BezierEvaluator(PointF controlPoint) {
        this.mControlPoint = controlPoint;
    }

    @Override
    public PointF evaluate(float t, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(t, startValue, mControlPoint, endValue);
    }

}

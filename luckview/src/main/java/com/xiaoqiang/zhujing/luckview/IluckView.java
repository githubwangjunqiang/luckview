package com.xiaoqiang.zhujing.luckview;

import java.util.List;

/**
 * Created by ${王俊强} on 2018/9/3.
 */
public interface IluckView {
    /**
     * 添加 单个奖品
     *
     * @param data
     */
    void addLuckData(LuckData data);

    /**
     * 添加多个奖品
     *
     * @param datas
     */
    void addLuckDatas(List<LuckData> datas);

    /**
     * 开始旋转
     */
    void startLuck();

    /**
     * 停止旋转 并且传入中将结果
     *
     * @param data
     */
    void stopLuck(LuckData data);

    /**
     * 是否在旋转
     *
     * @return
     */
    boolean isTruning();

    /**
     * 设置边框颜色 边框宽度默认paddingleft 的宽度
     *
     * @param color
     */
    void setBorderColor(int color);

    /**
     * 中奖结果是否高亮
     *
     * @param isUnColor false 为不适用 true 为使用
     */
    void setLuckisUnColor(boolean isUnColor);

    /**
     * 设置中将模块的变换颜色 高亮颜色 isUnColor为true时生效
     *
     * @param color 高亮颜色
     */
    void setLuckDataColor(int color);

    /**
     * 中奖结果是否突出 大小发生改变
     *
     * @param isUnSize false 为不用 true 为使用
     */
    void setLuckisUnSize(boolean isUnSize);



}

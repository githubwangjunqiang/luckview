package com.haowu.onionvideo.luck;

import java.util.List;

/**
 * Created by ${王俊强} on 2018/9/3.
 */
public interface IluckView {
    void addLuckData(LuckData data);

    void addLuckDatas(List<LuckData> datas);

    void startLuck();

    void stopLuck(LuckData data);

    boolean isTruning();
}

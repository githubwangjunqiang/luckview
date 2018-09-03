package com.haowu.onionvideo.luck;

import android.graphics.Bitmap;

/**
 * Created by ${王俊强} on 2018/9/3.
 */
public class LuckData {
    private String name;
    private int backColor;
    private Bitmap mBitmap;
    private int id = -1;

    public LuckData() {
    }

    public LuckData(String name, int backColor, Bitmap bitmap, int id) {
        this.name = name;
        this.backColor = backColor;
        mBitmap = bitmap;
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

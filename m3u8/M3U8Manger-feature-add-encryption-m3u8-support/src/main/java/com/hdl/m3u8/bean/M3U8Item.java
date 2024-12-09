package com.hdl.m3u8.bean;

/**
 * Created by suju by 2020/2/11
 */
public interface M3U8Item extends Comparable {

    String getFile();

    String getFileName();

    long getLongDate();

    float getSeconds();

    String getType();
}

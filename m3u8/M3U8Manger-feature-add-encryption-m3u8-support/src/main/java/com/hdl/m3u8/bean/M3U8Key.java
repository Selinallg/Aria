package com.hdl.m3u8.bean;

import android.support.annotation.NonNull;

/**
 * Created by suju by 2020/2/11
 */
public class M3U8Key implements M3U8Item {

    private String uri;

    private String iv;

    public M3U8Key(String uri, String iv) {
        this.uri = uri;
        this.iv = iv;
    }


    @Override
    public String getFile() {
        return uri;
    }

    @Override
    public String getFileName() {
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        if (fileName.contains("?")) {
            return fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public float getSeconds() {
        return 0;
    }

    @Override
    public long getLongDate() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }

    @Override
    public String getType() {
        return "key";
    }
}

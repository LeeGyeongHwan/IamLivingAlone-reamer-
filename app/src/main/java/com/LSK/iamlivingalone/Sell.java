package com.LSK.iamlivingalone;

import android.net.Uri;

public class Sell {


    public String title;
    public String content;
    public String enImg;
    public String uid;

    public Sell(String str) {
        this.enImg = str;
    }

    @Override
    public String toString() {
        return "Sell{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", enImg='" + enImg + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}

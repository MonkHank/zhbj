package com.seuic.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/5/26.
 * 组图对象
 */

public class PhotoBean {
    public PhotoData data;
    public class PhotoData{
        public ArrayList<PhotoNews> news;
    }
    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;

    }
}

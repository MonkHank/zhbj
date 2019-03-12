package com.seuic.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/4/25.
 * 分类信息的封装
 * 使用gson解析json时，对象书写技巧，缝{}创建对象，缝[]创建集合 ArrayList，
 * 所有字段名称要和json高度一致；
 *
 * {"retcode":200,
     "data":[
         {"id":10000,"title":"新闻","type":1,
             "children":[
                     {"id":10007,"title":"北京","type":1,"url":"/10007/list_1.json"},
                     {"id":10006,"title":"中国","type":1,"url":"/10006/list_1.json"},
                     {"id":10008,"title":"国际","type":1,"url":"/10008/list_1.json"},
                     {"id":10010,"title":"体育","type":1,"url":"/10010/list_1.json"},
                     {"id":10091,"title":"生活","type":1,"url":"/10091/list_1.json"},
                     {"id":10012,"title":"旅游","type":1,"url":"/10012/list_1.json"},
                     {"id":10095,"title":"科技","type":1,"url":"/10095/list_1.json"},
                     {"id":10009,"title":"军事","type":1,"url":"/10009/list_1.json"},
                     {"id":10093,"title":"时尚","type":1,"url":"/10093/list_1.json"},
                     {"id":10011,"title":"财经","type":1,"url":"/10011/list_1.json"},
                     {"id":10094,"title":"育儿","type":1,"url":"/10094/list_1.json"},
                     {"id":10105,"title":"汽车","type":1,"url":"/10105/list_1.json"}
                    ]
         },
         {"id":10002,"title":"专题","type":10,"url":"/10006/list_1.json","url1":"/10007/list1_1.json"},
         {"id":10003,"title":"组图","type":2,"url";:"/10008/list_1.json"},
         {"id":10004,"title":"互动","type":3,"excurl":"","dayurl":"","weekurl":""}
     ],
 "extend":[10007,10006,10008,10014,10012,10091,10009,10010,10095]
 }
 */

public class NewsMenu {

    public int retcode;
    public ArrayList<Integer> extend;

    public ArrayList<NewsMenuData> data;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }

    /**
     * 侧边栏菜单对象
     */
    public class NewsMenuData{
        public int id;
        public String title;
        public String type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 页签对象
     */
    public class NewsTabData{
        public int id;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}

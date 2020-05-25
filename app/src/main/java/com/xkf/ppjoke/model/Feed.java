package com.xkf.ppjoke.model;

import java.io.Serializable;

/**
 * author : xiakaifa
 * 2020/5/21
 */
public class Feed implements Serializable {
    public int id;
    public long itemId;
    public int itemType;
    public long createTime;
    public double duration;
    public String feeds_text;
    public long authorId;
    public String activityIcon;
    public String activityText;
    public int width;
    public int height;
    public String url;
    public String cover;

    public User author;
    public Comment topComment;
    public Ugc ugc;
}

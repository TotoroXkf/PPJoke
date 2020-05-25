package com.xkf.ppjoke.model;

import java.io.Serializable;

/**
 * author : xiakaifa
 * 2020/5/21
 */
public class Comment implements Serializable {
    public int id;
    public long itemId;
    public long commentId;
    public long userId;
    public int commentType;
    public long createTime;
    public int commentCount;
    public int likeCount;
    public String commentText;
    public String imageUrl;
    public String videoUrl;
    public int width;
    public int height;
    public boolean hasLiked;
    public User author;
    public Ugc ugc;
}

package com.xkf.ppjoke.model;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                likeCount == comment.likeCount &&
                hasLiked == comment.hasLiked &&
                author.equals(comment.author) &&
                ugc.equals(comment.ugc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, commentId, userId, commentType, createTime, commentCount, likeCount, commentText, imageUrl, videoUrl, width, height, hasLiked, author, ugc);
    }
}

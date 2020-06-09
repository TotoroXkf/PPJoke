package com.xkf.ppjoke.model;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return id == feed.id &&
                itemId == feed.itemId &&
                itemType == feed.itemType &&
                createTime == feed.createTime &&
                Double.compare(feed.duration, duration) == 0 &&
                feeds_text.equals(feed.feeds_text) &&
                authorId == feed.authorId &&
//                activityIcon.equals(feed.activityIcon) &&
//                activityText.equals(feed.activityText) &&
                width == feed.width &&
                height == feed.height &&
                url.equals(feed.url) &&
                cover.equals(feed.cover) &&
                author.equals(feed.author) &&
//                topComment.equals(feed.topComment) &&
                ugc.equals(feed.ugc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, itemType, createTime, duration, feeds_text, authorId, activityIcon, activityText, width, height, url, cover, author, topComment, ugc);
    }
}

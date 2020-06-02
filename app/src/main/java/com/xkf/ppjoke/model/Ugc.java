package com.xkf.ppjoke.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * author : xiakaifa
 * 2020/5/21
 */
public class Ugc implements Serializable {
    public int likeCount;
    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasdiss;
    public boolean hasLiked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ugc ugc = (Ugc) o;
        return likeCount == ugc.likeCount &&
                shareCount == ugc.shareCount &&
                commentCount == ugc.commentCount &&
                hasFavorite == ugc.hasFavorite &&
                hasdiss == ugc.hasdiss &&
                hasLiked == ugc.hasLiked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeCount, shareCount, commentCount, hasFavorite, hasdiss, hasLiked);
    }
}

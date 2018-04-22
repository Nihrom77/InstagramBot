package com.home.parameters;

import org.brunocvcunha.instagram4j.Instagram4j;

import java.util.List;

public class LikeTagParameters extends InstaParameters {
    private List<String> tags;
    private int maxPost;


    public LikeTagParameters(List<String> tags, int maxPost, Instagram4j instagram) {
        super(instagram);
        this.tags = tags;
        this.maxPost = maxPost;
    }

    public int getMaxPost() {
        return maxPost;
    }

    public List<String> getTags() {
        return tags;
    }
}

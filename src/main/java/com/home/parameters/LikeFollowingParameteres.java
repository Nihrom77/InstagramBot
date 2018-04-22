package com.home.parameters;

import org.brunocvcunha.instagram4j.Instagram4j;

public class LikeFollowingParameteres extends InstaParameters {

    private int lastN;
    private boolean postPhotoToVK;

    public LikeFollowingParameteres(Instagram4j instagram, boolean postPhotoToVK, int lastN) {
        super(instagram);
        this.lastN = lastN;
        this.postPhotoToVK = postPhotoToVK;
    }

    public int getLastN() {
        return lastN;
    }

    public boolean isPostPhotoToVK() {
        return postPhotoToVK;
    }
}

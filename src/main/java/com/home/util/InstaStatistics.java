package com.home.util;

/**
 * Статистика прогона лайкера.
 */
public class InstaStatistics {

    private int likedPhoto = 0;
    private int postedPhoto = 0;

    public InstaStatistics(int likedPhoto, int postedPhoto) {
        this.likedPhoto = likedPhoto;
        this.postedPhoto = postedPhoto;
    }

    public void addLikedPhoto() {
        likedPhoto++;
    }

    public void addPostedPhoto() {
        postedPhoto++;
    }

    public void resetStatistics() {
        postedPhoto = 0;
        likedPhoto = 0;
    }

    public int getLikedPhoto() {
        return likedPhoto;
    }

    public int getPostedPhoto() {
        return postedPhoto;
    }
}

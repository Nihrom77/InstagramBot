package com.home.commands;

import com.home.parameters.LikeTagParameters;
import com.home.util.InstaStatistics;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LikeTagOperation extends InstagramOperation {

    public LikeTagOperation(LikeTagParameters p) {
        super(p);
    }

    @Override public InstaStatistics execute() throws IOException {
        LikeTagParameters p = (LikeTagParameters) getParams();
        return likeInstagramTag(p.getTags(), p.getMaxPost(), p.getInstagram());
    }

    /**
     * Лайкает посты по тегу.
     */
    public InstaStatistics likeInstagramTag(List<String> tags, int maxPost, Instagram4j instagram)
        throws IOException {
        int counter = 0;
        int likedPhoto = 0;

        List<InstagramFeedResult> results = new ArrayList<>();
        for (String tag : tags) {
            InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramTagFeedRequest(tag));
            for (InstagramFeedItem feedResult : tagFeed.getItems()) {
                counter++;
                if (counter > maxPost) {
                    break;
                }
                System.out.println("Post ID: " + feedResult.getPk());
                if (!feedResult.has_liked) {
                    instagram.sendRequest(new InstagramLikeRequest(feedResult.getPk()));
                    likedPhoto++;
                    randomWait(3000, 5500);
                }
            }
        }
        return new InstaStatistics(likedPhoto, 0);
    }
}

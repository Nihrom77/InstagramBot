package com.home.commands;

import com.home.parameters.LikeFollowingParameteres;
import com.home.util.Constants;
import com.home.util.InstaFilter;
import com.home.util.InstaStatistics;
import com.home.vk.api.VkApi;
import org.apache.commons.io.FileUtils;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LikeFollowingOperation extends InstagramOperation {
    public List<String> likedUsers = new ArrayList<>();


    public LikeFollowingOperation(LikeFollowingParameteres p) {
        super(p);
    }



    @Override public InstaStatistics execute() throws IOException {
        LikeFollowingParameteres p = (LikeFollowingParameteres) getParams();
        return likeInstagramFollowing(p.getInstagram(), p.getLastN(), p.isPostPhotoToVK());
    }

    private InstaStatistics likeInstagramFollowing(Instagram4j instagram, final int lastN,
        boolean postPhotoToVK) throws IOException {

        InstaFilter filterNames = new InstaFilter();
        int likedPhoto = 0;
        int postedPhoto = 0;

        VkApi vk = null;
        if (postPhotoToVK) {
            vk = new VkApi();
        }

        List<String> ignoreName = Arrays.asList(new String[] {""});
        //Search user by handle
        InstagramSearchUsernameResult userResult =
            instagram.sendRequest(new InstagramSearchUsernameRequest(Constants.INSTAGRAM_LOGIN));
        System.out
            .println("ID for " + Constants.INSTAGRAM_LOGIN + " is " + userResult.getUser().getPk());
        System.out.println("Number of followers: " + userResult.getUser().follower_count);


        //        InstagramGetUserFollowersResult githubFollowers = instagram
        //            .sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
        InstagramGetUserFollowersResult followingResult = instagram
            .sendRequest(new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
        List<InstagramUserSummary> users = followingResult.getUsers();
        for (InstagramUserSummary user : users) {
            if (likedUsers.contains(user.getUsername())) {
                continue;
            }
            //            System.out.println("User " + user.getUsername() + " follows me!");
            System.out.println("I follow user " + user.getUsername());
            if (ignoreName.contains(user.getUsername())) {
                continue;
            }

            InstagramFeedResult userFeed =
                instagram.sendRequest(new InstagramUserFeedRequest(user.getPk()));
            if (!"fail".equals((userFeed).getStatus())) {
                int maxPostCounter = lastN;
                for (InstagramFeedItem feedResult : userFeed.getItems()) {
                    if (maxPostCounter > 0) {
                        maxPostCounter--;
                        System.out.println("Post ID: " + feedResult.getPk());
                        if (!feedResult.has_liked) {
                            File f = saveImage(feedResult, filterNames);
                            if (f != null && postPhotoToVK) {
                                String url =
                                    "https://www.instagram.com/p/" + feedResult.code + "/?taken-by="
                                        + feedResult.getUser().getUsername();
                                vk.postPhoto(f, url);
                                postedPhoto++;
                            }
                            instagram.sendRequest(new InstagramLikeRequest(feedResult.getPk()));
                            likedPhoto++;
                            randomWait(3000, 5500);
                        }
                    } else {
                        break;
                    }

                }
            }
            randomWait(1000, 1500);
            likedUsers.add(user.getUsername());
        }
        likedUsers.clear();
        return new InstaStatistics(likedPhoto, postedPhoto);
    }

    public File saveImage(InstagramFeedItem feedResult, InstaFilter filter) {
        File f = null;
        if (filter.isUserInFilter(feedResult.getUser().getUsername())
            && feedResult.getImage_versions2() != null) {
            ArrayList imagesArray = (ArrayList) feedResult.getImage_versions2().get("candidates");
            if (imagesArray != null && imagesArray.size() > 0 && imagesArray.get(0) != null
                && ((Map) imagesArray.get(0)).containsKey("url")) {
                Map urlMap = (Map) imagesArray.get(0);
                String urlStr = (String) urlMap.get("url");
                try {
                    LocalDateTime now = LocalDateTime.now();
                    String date = now.format(DateTimeFormatter.ISO_DATE);
                    String dirPath = Constants.APP_PHOTO_DIR + date + "/";
                    String filePath =
                        feedResult.getUser().getUsername() + "_" + feedResult.getId() + ".jpg";
                    f = new File(dirPath + filePath);
                    URL url = new URL(urlStr);
                    FileUtils.copyURLToFile(url, f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return f;
    }
}

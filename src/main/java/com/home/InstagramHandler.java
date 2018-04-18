package com.home;

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
import org.brunocvcunha.inutils4j.MyNumberUtils;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InstagramHandler {

    public void getFollowersExcludeFollowing() {

    }


    /**
     * Поставить лайки на последние N фотографий у тех, на кого я подписан.
     *
     * @param lastN
     * @throws java.io.IOException
     */
    public void likeLastPhotoOfMyFollowing(final int lastN, boolean postPhotoToVK)
        throws IOException {
        int likedPhoto = 0;
        int postedPhoto = 0;
        int tryCount = 8;
        int failCount = 0;
        List<String> likedUsers = new ArrayList<String>();
        InstaFilter filterNames = new InstaFilter();
        VkApi vk = null;
        if (postPhotoToVK) {
            vk = new VkApi();
        }
        boolean isFailed = true;
        for (int i = 0; i < tryCount && isFailed; ++i) {

            try {
                isFailed = false;
                // Login to instagram
                Instagram4j instagram = getInstagramConnection();


                List<String> ignoreName = Arrays.asList(new String[] {""});
                //Search user by handle
                InstagramSearchUsernameResult userResult = instagram
                    .sendRequest(new InstagramSearchUsernameRequest(Constants.INSTAGRAM_LOGIN));
                System.out.println(
                    "ID for " + Constants.INSTAGRAM_LOGIN + " is " + userResult.getUser().getPk());
                System.out.println("Number of followers: " + userResult.getUser().follower_count);


                //        InstagramGetUserFollowersResult githubFollowers = instagram
                //            .sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
                InstagramGetUserFollowersResult followingResult = instagram.sendRequest(
                    new InstagramGetUserFollowingRequest(userResult.getUser().getPk()));
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
                                            "https://www.instagram.com/p/" + feedResult.code
                                                + "/?taken-by=" + feedResult.getUser()
                                                .getUsername();
                                        vk.postPhoto(f, url);
                                        postedPhoto++;
                                    }
                                    instagram
                                        .sendRequest(new InstagramLikeRequest(feedResult.getPk()));
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
            } catch (SocketException e) {
                isFailed = true;
                failCount++;
                randomWait(5000, 10000);
            }
        }
        System.out.println("Failed " + failCount + " times");
        System.out.println("Today I liked " + likedPhoto + " photos from timeline. I'm good :)");
        if (postedPhoto > 0) {
            System.out.println(
                "Posted photo to vk: " + postedPhoto + ". Go to group: https://vk.com/club"
                    + Constants.VK_GROUP_ID);
        } else {
            System.out.println("None photo posted today.");
        }

    }

    /**
     * Login to instagram
     *
     * @return
     * @throws IOException
     */
    public static Instagram4j getInstagramConnection() throws IOException {

        Instagram4j instagram = Instagram4j.builder().username(Constants.INSTAGRAM_EMAIL)
            .password(Constants.INSTAGRAM_PASSWORD).build();
        instagram.setup();
        instagram.login();
        return instagram;
    }

    private static void randomWait(int from, int to) {
        try {
            Thread.sleep(MyNumberUtils.randomLongBetween(from, to));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    private void otherMethods() {
        //        //Search user by handle
        //        InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest("github"));
        //        System.out.println("ID for @github is " + userResult.getUser().getPk());
        //        System.out.println("Number of followers: " + userResult.getUser().getFollower_count());
        //
        //        //Follow user
        //        instagram.sendRequest(new InstagramFollowRequest(userResult.getUser().getPk()));
        //
        //        //Unfollow user
        //        instagram.sendRequest(new InstagramUnfollowRequest(userResult.getUser().getPk()));
        //
        //        //Get user followers
        //        InstagramGetUserFollowersResult githubFollowers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk()));
        //        List<InstagramUserSummary> users = githubFollowers.getUsers();
        //        for (InstagramUserSummary user : users) {
        //            System.out.println("User " + user.getUsername() + " follows Github!");
        //        }
        //


        //
        //        //Upload a video your feed
        //        instagram.sendRequest(new InstagramUploadPhotoRequest(
        //            new File("/tmp/file-to-upload.jpg"),
        //            "Posted with Instagram4j, how cool is that?"));
        //
        //        //Get feed for a hashtag
        //        InstagramFeedResult tagFeed = instagram.sendRequest(new InstagramTagFeedRequest("github"));
        //        for (InstagramFeedItem feedResult : tagFeed.getItems()) {
        //            System.out.println("Post ID: " + feedResult.getPk());
        //        }
        //
        //        //Perform a like operation for a media
        //        instagram.sendRequest(new InstagramLikeRequest(feedResult.getPk()));
        //
        //        //Add a comment for a media
        //        instagram.sendRequest(new iInstagramPostCommentRequest(feedResult.getPk(), "Hello! How are you?"));
    }
}

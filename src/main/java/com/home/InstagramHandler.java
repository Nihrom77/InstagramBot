package com.home;

import com.home.commands.InstagramOperation;
import com.home.util.Constants;
import com.home.util.InstaStatistics;
import org.brunocvcunha.instagram4j.Instagram4j;

import java.io.IOException;
import java.net.SocketException;

public class InstagramHandler {


    /**
     * Поставить лайки на последние N фотографий у тех, на кого я подписан.
     *
     * @throws java.io.IOException Исключение.
     */
    public void likeLastPhotos(InstagramOperation op) throws IOException {

        int tryCount = 8;
        int failCount = 0;

        InstaStatistics stat = new InstaStatistics(0, 0);

        boolean isFailed = true;
        for (int i = 0; i < tryCount && isFailed; ++i) {

            try {
                isFailed = false;
                // Login to instagram
                Instagram4j instagram = getInstagramConnection();
                if (op.getParams().getInstagram() == null) {
                    op.getParams().setInstagram(instagram);
                }
                stat = op.execute();

            } catch (SocketException e) {
                isFailed = true;
                failCount++;
                op.randomWait(5000, 10000);
            } catch (IOException e1) {
                e1.printStackTrace();
                break;
            }
        }
        System.out.println("Failed " + failCount + " times");
        System.out.println(
            "Today I liked " + stat.getLikedPhoto() + " photos from timeline. I'm good :)");
        if (stat.getPostedPhoto() > 0) {
            System.out.println("Posted photo to vk: " + stat.getPostedPhoto()
                + ". Go to group: https://vk.com/club" + Constants.VK_GROUP_ID.substring(1));
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
    public Instagram4j getInstagramConnection() throws IOException {

        Instagram4j instagram = Instagram4j.builder().username(Constants.INSTAGRAM_EMAIL)
            .password(Constants.INSTAGRAM_PASSWORD).build();
        instagram.setup();
        instagram.login();
        return instagram;
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

        //
        //        //Perform a like operation for a media
        //        instagram.sendRequest(new InstagramLikeRequest(feedResult.getPk()));
        //
        //        //Add a comment for a media
        //        instagram.sendRequest(new iInstagramPostCommentRequest(feedResult.getPk(), "Hello! How are you?"));
    }
}

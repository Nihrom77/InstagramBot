package com.home;

import com.home.commands.LikeFollowingOperation;
import com.home.commands.LikeTagOperation;
import com.home.parameters.LikeFollowingParameteres;
import com.home.parameters.LikeTagParameters;

import java.util.Arrays;


public class BotLauncher {

    public static void main(String[] args) throws Exception {

        InstagramHandler insta = new InstagramHandler();

        boolean likeFolowing = false;
        if (likeFolowing) {
            insta.likeLastPhotos(
                new LikeFollowingOperation(new LikeFollowingParameteres(null, true, 5)));
        }

        boolean likeTags = true;
        if (likeTags) {
            insta.likeLastPhotos(
                new LikeTagOperation(new LikeTagParameters(Arrays.asList("библионочь"), 70, null)));
        }
    }


}

package com.home;

public class BotLauncher {

    public static void main(String[] args) throws Exception {

        InstagramHandler insta = new InstagramHandler();
        insta.likeLastPhotoOfMyFollowing(5, true);


    }


}

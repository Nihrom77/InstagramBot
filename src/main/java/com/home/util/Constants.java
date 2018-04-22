package com.home.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public  class Constants {
    private static Properties prop;


    static {
        prop = new Properties();

        try {
            InputStream input = new FileInputStream("target/classes/Pass.properties");


            // load a properties file
            prop.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String INSTAGRAM_LOGIN = prop.getProperty("instagram.login");

    public static String INSTAGRAM_EMAIL = prop.getProperty("instagram.email");

    public static String INSTAGRAM_PASSWORD = prop.getProperty("instagram.password");

    public static String VK_APP_ID = prop.getProperty("vk.app_id");

    public static String VK_CLIENT_SECRET = prop.getProperty("vk.CLIENT_SECRET");

    public static String VK_REDIRECT_URI = prop.getProperty("vk.REDIRECT_URI");

    public static String VK_CODE = prop.getProperty("vk.CODE");
    public static String VK_GROUP_ACCESS_TOKEN = prop.getProperty("vk.GROUP_ACCESS_TOKEN");

    public static String VK_USER_ID = prop.getProperty("vk.USER_ID");

    public static String VK_USER_ACCESS_TOKEN = prop.getProperty("vk.USER_ACCESS_TOKEN");

    public static String VK_GROUP_ID = prop.getProperty("vk.GROUP_ID");

    public static String APP_PHOTO_DIR = prop.getProperty("app.photo_dir");


}


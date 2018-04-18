package com.home.vk.api;

import com.home.Constants;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.objects.photos.responses.WallUploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;

import java.io.File;
import java.util.List;





public class VkApi {

    VkConnection connection;


    public VkApi() {
        connection = new VkConnection();
    }

    public void postPhoto(File file, String urlStr) {
        try {
            postPhoto(file, urlStr, connection.getVk(), connection.getActor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postPhoto(File file, String urlStr, VkApiClient vk, UserActor actor)
        throws ApiException, ClientException {

        if (file.exists()) {
            PhotoUpload serverResponse = vk.photos().getWallUploadServer(actor).execute();
            WallUploadResponse uploadResponse =
                vk.upload().photoWall(serverResponse.getUploadUrl(), file).execute();
            List<Photo> photoList = vk.photos().saveWallPhoto(actor, uploadResponse.getPhoto())
                .server(uploadResponse.getServer()).hash(uploadResponse.getHash()).execute();

            Photo photo = photoList.get(0);
            String attachId = "photo" + photo.getOwnerId() + "_" + photo.getId();
            PostResponse postResponse =
                vk.wall().post(actor).ownerId(Integer.parseInt(Constants.VK_GROUP_ID))
                    .message(urlStr).attachments(attachId).fromGroup(Boolean.TRUE).execute();
        } else {
            System.out.println("File not exists");
        }
    }
}

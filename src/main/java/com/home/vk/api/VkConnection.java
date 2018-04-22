package com.home.vk.api;

import com.home.util.Constants;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

public class VkConnection {



    private UserActor actor;

    private VkApiClient vk;

    public VkConnection() {
        connectVK();
    }


    public void connectVK() {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(Integer.parseInt(Constants.VK_USER_ID)  ,
            Constants.VK_USER_ACCESS_TOKEN );
    }

    public UserActor getActor() {
        return actor;
    }

    public VkApiClient getVk() {
        return vk;
    }

    private void auth() {
        //        UserAuthResponse authResponse = vk.oauth()
        //            .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, CODE)
        //            .execute();
        //

        //
        //        System.out.println(vk.groups().get(actor).filter(GroupsGetFilter.ADMIN).execute());
        //        GroupActor groupActor = new GroupActor(GROUP_ID, GROUP_ACCESS_TOKEN);
        //        System.out.println(actor.getGroupId());

        //        vk.wall().post(actor).message("Hello world").execute();

    }
}

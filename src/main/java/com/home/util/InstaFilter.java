package com.home.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstaFilter {

    private List<String> users = new ArrayList<>();

    public InstaFilter() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("target/classes/MyFavoriteUsers.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray users1 = (JSONArray) jsonObject.get("users");


            Iterator<String> iterator = users1.iterator();
            while (iterator.hasNext()) {
                users.add(iterator.next());
            }


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public boolean isUserInFilter(String user) {
        return users.contains(user);
    }
}

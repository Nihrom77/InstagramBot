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
import java.util.Map;

public class InstaFilter {

    private List<String> users = new ArrayList<>();

    public InstaFilter() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("target/classes/MyFavoriteUsers.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray users1 = (JSONArray) jsonObject.get("users");


            Iterator<Map<String, String>> iterator = users1.iterator();
            while (iterator.hasNext()) {
                Map<String, String> user = iterator.next();
                String userStr = user.keySet().stream().findAny().orElse("");
                users.add(userStr);
            }


            /*
               HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }

        System.out.println("json : "+jObject);
        System.out.println("map : "+map);
             */

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public boolean isUserInFilter(String user) {
        return users.contains(user);
    }
}

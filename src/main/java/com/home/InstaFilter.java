package com.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstaFilter {
    String[] myUsers= {"gusiona_lapchataya","_ilya_.nik","sveta.zim.sz","senkovskiy","iriyasan","helly___belly",
    "khan_shu","nookerrr","zhenya.voynov","anastasya.dyachenko","ilya_a_orlov","kostiko8","artyom.zemskov",
    "natalia.malcina","ilya2710","vanadium50","shib_vit","anastasilz","alwayshelix","gusiona_lapchataya",
    "dyakov.vladimir","irashmatova","vladimir_ignatoff","_artartem"};
    List<String> users;

    public InstaFilter(){
        users = Arrays.asList(myUsers);
    }

    public boolean isUserInFilter(String user){
        return users.contains(user);
    }
}

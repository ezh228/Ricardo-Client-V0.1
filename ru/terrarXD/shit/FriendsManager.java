package ru.terrarXD.shit;

import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 13:48 of 25.04.2023
 */
public class FriendsManager {
    public ArrayList<String> friends = new ArrayList<>();
    public FriendsManager(){

    }

    public void addFriend(String name){
        for (String names : friends){
            if (names.equalsIgnoreCase(name)){
                return;
            }
        }
        friends.add(name);
    }
    public void delFriend(String name){
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).equals(name)){
                friends.remove(i);
                return;
            }
        }


    }

    public boolean isFriend(String name){
        for (String names : friends){
            if (names.equals(name )){
                return true;
            }
        }
        return false;
    }
}

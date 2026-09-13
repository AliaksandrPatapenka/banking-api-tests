package com.apiAuto.presentation.models.users;

import java.util.List;

public class UserCreate {
    private String login;
    private String name;
    private int age;
    private String gender;
    private String hairColor;
    private List<String> friends;

    public UserCreate() {
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getHairColor() {
        return hairColor;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}

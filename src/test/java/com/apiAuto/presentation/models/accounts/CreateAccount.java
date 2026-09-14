package com.apiAuto.presentation.models.accounts;

public class CreateAccount {
    private String userLogin;

    public CreateAccount() {
    }

    public String getUserLogin(){return userLogin;}

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}

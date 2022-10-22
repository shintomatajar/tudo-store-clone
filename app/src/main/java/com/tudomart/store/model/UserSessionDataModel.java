package com.tudomart.store.model;

public class UserSessionDataModel {

    String token, userId, firstName, email, phoneNo, shop_id;
    String[] shopList, tudoShopList;

    public UserSessionDataModel(String token, String userId, String firstName, String email, String phoneNo,
                                String shop_id, String[] shopList) {
        this.token = token;
        this.userId = userId;
        this.firstName = firstName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.shop_id = shop_id;
        this.shopList = shopList;

    }

    public String getToken() {
        return "bearer " + token;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String[] getShopList() {
        return shopList;
    }


}

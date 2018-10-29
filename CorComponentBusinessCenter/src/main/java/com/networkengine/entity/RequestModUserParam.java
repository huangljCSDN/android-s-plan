package com.networkengine.entity;

/**
 * Created by pengpeng on 16/12/8.
 */

public class RequestModUserParam {

    private String id;

    private String phone;
    private String imageAddress;
    private String email;

    private String telephone;

    private String signature;

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public String getSignature() { return signature; }

    public String getTelephone() { return telephone; }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

}

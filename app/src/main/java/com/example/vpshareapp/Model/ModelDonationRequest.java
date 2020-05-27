package com.example.vpshareapp.Model;

public class ModelDonationRequest {
String uid,Name,Email,Address,RequestedDonationStuff;

    public ModelDonationRequest(String uid, String name, String email, String address, String requestedDonationStuff) {
        this.uid = uid;
        Name = name;
        Email = email;
        Address = address;
        RequestedDonationStuff = requestedDonationStuff;
    }

    public ModelDonationRequest() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRequestedDonationStuff() {
        return RequestedDonationStuff;
    }

    public void setRequestedDonationStuff(String requestedDonationStuff) {
        RequestedDonationStuff = requestedDonationStuff;
    }
}


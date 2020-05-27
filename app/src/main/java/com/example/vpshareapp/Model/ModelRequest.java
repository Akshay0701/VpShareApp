package com.example.vpshareapp.Model;

public class ModelRequest {
    String userid,username,useremail,useraddress,isBagSended;


    public ModelRequest(String userid, String username, String useremail, String useraddress, String isBagSended) {
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;
        this.useraddress = useraddress;
        this.isBagSended = isBagSended;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getIsBagSended() {
        return isBagSended;
    }

    public void setIsBagSended(String isBagSended) {
        this.isBagSended = isBagSended;
    }

    public ModelRequest() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }
}

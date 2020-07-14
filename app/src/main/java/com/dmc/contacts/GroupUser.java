package com.dmc.contacts;

import java.io.Serializable;

class GroupUser implements Serializable {

    private String userName;
    private String phone;
    private String nameSub;
    private String otherInfo;

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNameSub() {
        return nameSub;
    }

    public void setNameSub(String nameSub) {
        this.nameSub = nameSub;
    }
}

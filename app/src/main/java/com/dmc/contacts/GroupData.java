package com.dmc.contacts;

import java.util.List;

class GroupData {

    /**
     * goupName : 顶级部门
     * goupId : asdaskldalskd
     * groupUsers : [{"userName":"测试人员1","userPhone":"123123123123"},{"userName":"测试人员2","userPhone":"2222222"},{"userName":"测试人员3","userPhone":"23232323"}]
     * groupChilds : []
     */

    private String goupName;
    private String goupId;
    private List<GroupUser> groupUsers;
    private List<GroupData> groupChilds;

    public String getGoupName() {
        return goupName;
    }

    public void setGoupName(String goupName) {
        this.goupName = goupName;
    }

    public String getGoupId() {
        return goupId;
    }

    public void setGoupId(String goupId) {
        this.goupId = goupId;
    }

    public List<GroupUser> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public List<GroupData> getGroupChilds() {
        return groupChilds;
    }

    public void setGroupChilds(List<GroupData> groupChilds) {
        this.groupChilds = groupChilds;
    }
}

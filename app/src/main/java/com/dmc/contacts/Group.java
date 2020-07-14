package com.dmc.contacts;

import java.io.Serializable;
import java.util.List;

class Group implements Serializable {
    //如果是组织，那么这里就有数据
    private String groupId;//组织id
    private String groupName;//组织名称
    private List<Group> groupChilds;//组织的下级

    //如果不是组织，那么这里的表示当前item是用户
    private GroupUser groupUser;//个人用户信息

    //----自定义参数-----//
    private int selectCount;//选择的数量
    private int childCount;//下级人数总数
    private boolean isGroup = false;//是否是组织
    private boolean isChecked = false;//是否选择

    public void toggle() {
        isChecked = !isChecked;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Group> getGroupChilds() {
        return groupChilds;
    }

    public void setGroupChilds(List<Group> groupChilds) {
        this.groupChilds = groupChilds;
    }

    public GroupUser getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(GroupUser groupUser) {
        this.groupUser = groupUser;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

package com.dmc.contacts;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GroupSelectActivity extends Activity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnSure;
    private TextView numberTip;

    private List<GroupListView> groupListViews = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private int selectPosition;

    private List<Group> groupDatas = new ArrayList<>();
    private List<GroupUser> selectUsers = new ArrayList<>();
    private boolean selectSingle = false;

    private GroupData rootGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);

        selectSingle = getIntent().getBooleanExtra("single", false);
        if (getIntent().getSerializableExtra("data") != null)
            groupDatas = (List<Group>) getIntent().getSerializableExtra("data");
        //单选的话下面的确定按钮就不需要了
        findViewById(R.id.bottom_layout).setVisibility(selectSingle ? View.GONE : View.VISIBLE);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
        btnSure = findViewById(R.id.btn_sure);
        numberTip = findViewById(R.id.number_tip);
        initView();

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                doBusiness();
            }
        });
    }

    protected void initView() {
        //配置ViewPager
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("data", (Serializable) groupDatas);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    protected void doBusiness() {
        if (groupDatas == null || groupDatas.size() == 0) {
            //模拟数据
            rootGroup = DataUtil.getData();
            groupDatas.addAll(dealData(rootGroup));
        } else {
            refreshSelect(groupDatas);
            numberTip.setText(getString(R.string.select_number_tip, selectUsers.size()));
        }
        createGroupItemView(null);
    }

    private void refreshSelect(List<Group> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGroupChilds() != null && list.get(i).getGroupChilds().size() > 0) {
                refreshSelect(list.get(i).getGroupChilds());
            }
            if (!list.get(i).isGroup() && list.get(i).isChecked()) {
                selectUsers.add(list.get(i).getGroupUser());
            }
        }
    }

    /**
     * 处理数据
     *
     * @param group
     * @return
     */
    private List<Group> dealData(GroupData group) {
        List<Group> groups = new ArrayList<>();
        if (group.getGroupChilds() != null && group.getGroupChilds().size() > 0) {
            for (int i = 0; i < group.getGroupChilds().size(); i++) {
                Group childIndex = new Group();
                childIndex.setGroupName(group.getGroupChilds().get(i).getGoupName());
                childIndex.setGroupId(group.getGroupChilds().get(i).getGoupId());
                childIndex.setChildCount(getChildNumber(group.getGroupChilds().get(i)));
                childIndex.setGroupChilds(dealData(group.getGroupChilds().get(i)));
                childIndex.setGroup(true);
                groups.add(childIndex);
            }
        }
        if (group.getGroupUsers() != null && group.getGroupUsers().size() > 0) {
            for (int i = 0; i < group.getGroupUsers().size(); i++) {
                Group item = new Group();
                item.setGroup(false);
                item.setGroupUser(group.getGroupUsers().get(i));
                groups.add(item);
            }
        }
        return groups;
    }

    private int getChildNumber(GroupData group) {
        int allNumber = 0;
        if (group.getGroupUsers() != null) {
            allNumber = group.getGroupUsers().size();
        }
        if (group.getGroupChilds() == null || group.getGroupChilds().size() == 0) {
            return allNumber;
        }
        for (int i = 0; i < group.getGroupChilds().size(); i++) {
            allNumber = allNumber + getChildNumber(group.getGroupChilds().get(i));
        }
        return allNumber;
    }


    /**
     * 添加视图
     */
    private void createGroupItemView(Group group) {
        GroupListView groupListView = new GroupListView(this, true, !selectSingle);
        groupListView.setTitle(group == null ? rootGroup.getGoupName() : group.getGroupName());
        groupListView.setPrentId(group == null ? "" : group.getGroupId());
        groupListView.setOnItemClickListener(new GroupListView.OnItemClickListener() {
            @Override
            public void onItemClick(Group groupListItem) {
                if (selectPosition < groupListViews.size() - 1) {
                    List<GroupListView> tempList = new ArrayList<>();
                    for (int i = (selectPosition + 1); i < groupListViews.size(); i++) {
                        tempList.add(groupListViews.get(i));
                    }
                    groupListViews.removeAll(tempList);
                    viewPagerAdapter.notifyDataSetChanged();
                }
                if (groupListItem != null) {
                    createGroupItemView(groupListItem);
                }
            }
        });
        groupListView.setOnItemCheckedListener(new GroupListView.OnItemCheckedListener() {

            @Override
            public void checkedAll(List<Group> groups, boolean isChecked) {
                selectUsers.clear();
                dealCheckedAllChild(groups, isChecked);
                refreshSelectNumber(groupDatas);
                Log.d("selectUsers", " selectUsers size :" + selectUsers.size());
                for (int i = 0; i < groupListViews.size(); i++) {
                    groupListViews.get(i).refresh();
                }
                numberTip.setText(getString(R.string.select_number_tip, selectUsers.size()));
            }

            @Override
            public void checkedItem() {
                selectUsers.clear();
                refreshSelectNumber(groupDatas);
                numberTip.setText(getString(R.string.select_number_tip, selectUsers.size()));
                Log.d("selectUsers", " selectUsers size :" + selectUsers.size());
                for (int i = 0; i < groupListViews.size(); i++) {
                    groupListViews.get(i).refresh();
                }

                //如果是单选，这里就直接返回了
                if (selectSingle){
                    Intent intent = new Intent();
                    intent.putExtra("data", (Serializable) groupDatas);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        });
        groupListViews.add(groupListView);
        groupListView.initData(group == null ? groupDatas : group.getGroupChilds());
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(selectPosition + 1, true);
    }

    /**
     * 刷新下哪些选择的
     *
     * @param list
     */
    private void refreshSelectNumber(List<Group> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGroupChilds() != null && list.get(i).getGroupChilds().size() > 0) {
                list.get(i).setSelectCount(getChildSelectNumber(list.get(i).getGroupChilds()));
                refreshSelectNumber(list.get(i).getGroupChilds());
            }
            if (!list.get(i).isGroup() && list.get(i).isChecked()) {
                selectUsers.add(list.get(i).getGroupUser());
            }
        }
    }

    /**
     * 获取选择的人数
     *
     * @param groupList
     * @return
     */
    private int getChildSelectNumber(List<Group> groupList) {
        int selectNumber = 0;
        if (groupList != null && groupList.size() > 0) {
            for (int j = 0; j < groupList.size(); j++) {
                if (groupList.get(j).isGroup()) {
                    selectNumber = selectNumber + getChildSelectNumber(groupList.get(j).getGroupChilds());
                } else {
                    if (groupList.get(j).isChecked()) {
                        selectNumber++;
                    }
                }
            }
        }
        return selectNumber;
    }

    private void dealCheckedAllChild(List<Group> items, boolean isChecked) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getGroupChilds() != null) {
                dealCheckedAllChild(items.get(i).getGroupChilds(), isChecked);
            }
            items.get(i).setChecked(isChecked);
        }
    }


    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return groupListViews.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return groupListViews.get(position).getTitle();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(groupListViews.get(position));
            return groupListViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((GroupListView) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (object instanceof GroupListView) {
                if (groupListViews.contains(object)) {
                    return groupListViews.indexOf(object);
                } else {
                    return POSITION_NONE;
                }
            }
            return super.getItemPosition(object);
        }
    }
}

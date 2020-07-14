package com.dmc.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


public class GroupListView extends ConstraintLayout {
    private Context mContext;
    private RecyclerView recyclerView;
    private CheckBox checkBoxAll;

    private List<Group> mGroupListItems = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private OnItemClickListener onItemClickListener;
    private OnItemCheckedListener onItemCheckedListener;
    private String title;

    private Group selectItem;

    private boolean showChecked = false;
    private boolean showCall = false;

    private String prentId;

    public interface OnItemClickListener {
        void onItemClick(Group groupListItem);
    }

    public interface OnItemCheckedListener {
        void checkedAll(List<Group> groups, boolean isChecked);

        void checkedItem();
    }

    public GroupListView(@NonNull Context context, boolean showCall, boolean showChecked) {
        super(context);
        this.showChecked = showChecked;
        this.showCall = showCall;
        initView(context);
    }

    public GroupListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GroupListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_group_selector, this, true);
        recyclerView = view.findViewById(R.id.recycler_view);
        checkBoxAll = view.findViewById(R.id.check_all);
        itemAdapter = new ItemAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        checkBoxAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mGroupListItems.size(); i++) {
                    mGroupListItems.get(i).setChecked(checkBoxAll.isChecked());
                }
                if (onItemCheckedListener != null) {
                    onItemCheckedListener.checkedAll(mGroupListItems, checkBoxAll.isChecked());
                }
            }
        });
        checkBoxAll.setVisibility(showChecked ? View.VISIBLE : View.GONE);
    }

    /**
     * 点击组织
     *
     * @param i
     */
    private void clickGroupItem(int i) {
        //记录下选择的下级组织
        selectItem = mGroupListItems.get(i);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(mGroupListItems.get(i));
        }
    }

    /**
     * 选择某一个item
     *
     * @param i
     */
    private void checkItem(int i) {
        mGroupListItems.get(i).toggle();
        int selectCount = 0;
        for (int j = 0; j < mGroupListItems.size(); j++) {
            if (mGroupListItems.get(j).isChecked()) {
                selectCount++;
            }
        }
        checkBoxAll.setChecked(selectCount == mGroupListItems.size());
        if (onItemCheckedListener != null) {
            if (mGroupListItems.get(i).isGroup()) {
                onItemCheckedListener.checkedAll(mGroupListItems.get(i).getGroupChilds(), mGroupListItems.get(i).isChecked());
            } else {
                onItemCheckedListener.checkedItem();
            }
        }
    }


    private class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int ITEM_GROUP = 0;
        private int ITEM_USER = 1;
        private Context mContext;

        ItemAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == ITEM_GROUP) {
                return new GroupViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_group_item, viewGroup, false));
            } else {
                return new UserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_group_user_item, viewGroup, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final Group item = mGroupListItems.get(position);
            if (getItemViewType(position) == ITEM_GROUP) {
                CheckBox checkBox = ((GroupViewHolder) holder).checkGroup;
                checkBox.setVisibility(showChecked ? View.VISIBLE : View.GONE);
                checkBox.setChecked(item.isChecked());
                checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkItem(position);
                    }
                });
                TextView groupName = ((GroupViewHolder) holder).groupName;
                TextView childNext = ((GroupViewHolder) holder).childNext;
                groupName.setTextColor(item.isChecked() ? ContextCompat.getColor(mContext, R.color.grey_500) : ContextCompat.getColor(mContext, R.color.grey_900));
                childNext.setTextColor(item.isChecked() ? ContextCompat.getColor(mContext, R.color.grey_500) : ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                if (showChecked) {
                    childNext.setText("下级");
                    groupName.setText(String.format("%s(%d/%d)", item.getGroupName(), item.getSelectCount(), item.getChildCount()));
                    childNext.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (item.isChecked() || item.getChildCount() == 0)
                                return;
                            clickGroupItem(position);
                        }
                    });
                } else {
                    childNext.setText("");
                    groupName.setText(String.format("%s（%d)", item.getGroupName(), item.getChildCount()));
                }
                ((GroupViewHolder) holder).itemGroupLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //如果是显示了选择，这里点击item就是选择的动作
                        if (showChecked) {
                            checkItem(position);
                        } else {
                            clickGroupItem(position);
                        }
                    }
                });
            } else {
                CheckBox checkBox = ((UserViewHolder) holder).checkUser;
                checkBox.setVisibility(showChecked ? View.VISIBLE : View.GONE);
                checkBox.setChecked(item.isChecked());
                checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkItem(position);
                    }
                });
                TextView userName = ((UserViewHolder) holder).name;
                userName.setText(item.getGroupUser().getName());
                userName.setTextColor(item.isChecked() ? ContextCompat.getColor(mContext, R.color.grey_500) : ContextCompat.getColor(mContext, R.color.grey_900));
//                GlideUtil.setIcon(mContext, ((UserViewHolder) holder).icon, item.getUserInfo().getUserPhotoUrl());
                ((UserViewHolder) holder).btnPhone.setVisibility(showCall ? View.VISIBLE : View.GONE);
                ((UserViewHolder) holder).btnPhone.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext,"打电话",Toast.LENGTH_SHORT).show();
                    }
                });
                ((UserViewHolder) holder).itemLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkItem(position);
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mGroupListItems.get(position).getGroupUser() == null) {
                return ITEM_GROUP;
            }
            return ITEM_USER;
        }

        @Override
        public int getItemCount() {
            return mGroupListItems.size();
        }


        private class GroupViewHolder extends RecyclerView.ViewHolder {
            TextView groupName, childNext;
            ConstraintLayout itemGroupLayout;
            CheckBox checkGroup;

            GroupViewHolder(@NonNull View itemView) {
                super(itemView);
                itemGroupLayout = itemView.findViewById(R.id.item_group_layout);
                groupName = itemView.findViewById(R.id.item_group_name);
                checkGroup = itemView.findViewById(R.id.item_group_check);
                childNext = itemView.findViewById(R.id.child_next);
            }
        }

        private class UserViewHolder extends RecyclerView.ViewHolder {
            ConstraintLayout itemLayout;
            ImageView icon, btnPhone;
            TextView name;
            CheckBox checkUser;

            UserViewHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = itemView.findViewById(R.id.item_layout);
                icon = itemView.findViewById(R.id.item_icon);
                name = itemView.findViewById(R.id.item_name);
                btnPhone = itemView.findViewById(R.id.item_call_phone);
                checkUser = itemView.findViewById(R.id.item_check);
            }
        }
    }

    private void initCheckAll() {
        int selectCount = 0;
        for (int j = 0; j < mGroupListItems.size(); j++) {
            if (mGroupListItems.get(j).isGroup()) {
                if (mGroupListItems.get(j).getChildCount() > 0) {
                    mGroupListItems.get(j).setChecked(mGroupListItems.get(j).getSelectCount() == mGroupListItems.get(j).getChildCount());
                }
            }
            if (mGroupListItems.get(j).isChecked()) {
                selectCount++;
            }
        }
        checkBoxAll.setChecked(selectCount == mGroupListItems.size());
    }

    /**
     * 设置数据
     *
     * @param groupListItems
     */
    public void initData(List<Group> groupListItems) {
        this.mGroupListItems.addAll(groupListItems);
        refresh();
    }

    public void refresh() {
        initCheckAll();
        itemAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrentId() {
        return prentId;
    }

    public void setPrentId(String prentId) {
        this.prentId = prentId;
    }

    /**
     * 获取下级组织
     *
     * @return
     */
    public Group getSelectItem() {
        return selectItem;
    }

    public List<Group> getGroupListItems() {
        return mGroupListItems;
    }

}

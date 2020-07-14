package com.dmc.contacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_GROUP = 1001;
    private TextView show;

    private List<Group> groupDatas = new ArrayList<>();//选择的集合
    private List<GroupUser> selectUsers = new ArrayList<>();//选择的人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.select_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupSelectActivity.class);
                intent.putExtra("single", true);
                startActivityForResult(intent, REQUEST_CODE_SELECT_GROUP);
            }
        });
        findViewById(R.id.select_users).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupSelectActivity.class);
                intent.putExtra("data", (Serializable) groupDatas);
                intent.putExtra("single", false);
                startActivityForResult(intent, REQUEST_CODE_SELECT_GROUP);
            }
        });
        show = findViewById(R.id.show);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode == REQUEST_CODE_SELECT_GROUP) {
                groupDatas = (List<Group>) data.getSerializableExtra("data");
                selectUsers.clear();
                refreshSelect(groupDatas);
                if (selectUsers.size() == 0) {
                    show.setText("");
                    return;
                }
                StringBuilder builder =new StringBuilder();
                for (int i = 0; i < selectUsers.size(); i++) {
                    builder.append(selectUsers.get(i).getName()).append("、");
                }
                //选取范围
                show.setText(String.format("%s\n%s", getString(R.string.select_number_tip, selectUsers.size()), builder.toString()));
            }
        }
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
}
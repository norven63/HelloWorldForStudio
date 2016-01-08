package com.myAndroid.helloworld.activity.dataBinding;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.myAndroid.helloworld.R;
import com.myAndroid.helloworld.databinding.MyDataBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 参阅官网：http://developer.android.com/tools/data-binding/guide.html#studio_support
 */
public class DataBindingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);

        User user = new User("first", "lastName", true);
        binding.setUser(user);

        MyClickHandler clickHandler = new MyClickHandler(user);
        binding.setClickHandler(clickHandler);

        List<User> users = new ArrayList<>();
        users.add(new User("e1_f", "e1_l", true));
        users.add(new User("e2_f", "e2_l", false));
        users.add(new User("e3_f", "e3_l", true));
        binding.setUserList(users);

        User userFromBinding = binding.getUser();//啥也别说了，之前都set过了，这里当然也能get。同理，其他任何在xml中申明过的variable变量也都可以get出来

        RecyclerView recyclerView = binding.recyclerView;//xml布局中，生命id为recyclerView的控件会变成binding的一个成员变量
        recyclerView.setAdapter(new DataBindingRecyclerAdapter(new String[]{"aaa", "bbb", "ccc", "ddd", "aaa", "bbb", "ccc", "ddd"}));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}

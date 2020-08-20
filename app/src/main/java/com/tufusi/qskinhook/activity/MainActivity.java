package com.tufusi.qskinhook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tufusi.qskinhook.R;
import com.tufusi.qskinhook.adapter.MyFragmentPageAdapter;
import com.tufusi.qskinhook.fragment.MusicFragment;
import com.tufusi.qskinhook.fragment.RadioFragment;
import com.tufusi.qskinhook.fragment.VideoFragment;
import com.tufusi.qskinhook.widget.MyTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyTabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        fragmentList.add(MusicFragment.getInstance());
        fragmentList.add(VideoFragment.getInstance());
        fragmentList.add(RadioFragment.getInstance());

        titleList.add("音乐");
        titleList.add("MV");
        titleList.add("电台");

        MyFragmentPageAdapter adapter = new MyFragmentPageAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 进入换肤
     */
    public void skinSelect(View view){
        startActivity(new Intent(MainActivity.this, SkinActivity.class));
    }
}
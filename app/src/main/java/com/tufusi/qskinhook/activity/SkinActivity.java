package com.tufusi.qskinhook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tufusi.qskin.SkinManager;
import com.tufusi.qskinhook.R;

public class SkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void change(View view) {
        SkinManager.getInstance().loadSkinBag("/data/data/com.tufusi.qskinhook/SkinBag-debug.apk");
    }

    public void restore(View view) {

    }
}
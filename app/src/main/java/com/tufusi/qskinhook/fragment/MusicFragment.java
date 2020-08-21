package com.tufusi.qskinhook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tufusi.qskinhook.R;

/**
 * Created by 鼠夏目 on 2020/8/20.
 *
 * @author 鼠夏目
 * @description
 */
public class MusicFragment extends Fragment {

    public static Fragment getInstance() {
        MusicFragment sf = new MusicFragment();
        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        return view;
    }

    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);
    }
}
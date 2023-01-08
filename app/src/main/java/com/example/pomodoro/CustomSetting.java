package com.example.pomodoro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pomodoro.databinding.CustomSettingBinding;
import com.example.pomodoro.databinding.PomodoroTimerBinding;


public class CustomSetting extends Fragment {

    private CustomSettingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CustomSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
package com.example.pomodoro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.databinding.StatisticsPageBinding;

public class StatisticsPage extends Fragment {

    protected static int pomodoroTotal;
    protected static int workTotal;
    protected static int breakTotal;

    protected static int pomodoroCycles = 0;

    private StatisticsPageBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = StatisticsPageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateStats();
        updateBadges();

    }

    private void updateBadges() {
        if (pomodoroCycles >= 10) {
            binding.badgeThreeInitial.setAlpha((float) 1.00);
        } else if (pomodoroCycles >= 5) {
            binding.badgeTwoInitial.setAlpha((float) 1.00);
        } else if (pomodoroCycles >= 1) {
            binding.badgeOneInitial.setAlpha((float) 1.00);
        }
    }

    private void updateStats() {
        String workTotalString = String.valueOf(workTotal) + "s";
        String breakTotalString = String.valueOf(breakTotal) + "s";

        binding.pomodoroTotal.setText(String.valueOf(pomodoroTotal));
        binding.workTotal.setText(workTotalString);
        binding.breakTotal.setText(breakTotalString);
    }
}

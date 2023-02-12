package com.example.pomodoro;

import static com.example.pomodoro.UserAccount.breakTotal;
import static com.example.pomodoro.UserAccount.pomodoroCycles;
import static com.example.pomodoro.UserAccount.pomodoroTotal;
import static com.example.pomodoro.UserAccount.workTotal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.databinding.ProfilePageBinding;

public class ProfilePage extends Fragment {

    private ProfilePageBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ProfilePageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateProfile();
        updateStats();
        updateBadges();
        showToolTips();
    }

    private void updateProfile() {
        binding.iconProfileImage.setImageURI(UserAccount.getUriImage());
        binding.nameProfileText.setText(UserAccount.getUsername());
        binding.tomatoesUserText.setText(String.valueOf(UserAccount.getTomatoes()));
    }

    private void showToolTips() {
        TooltipCompat.setTooltipText(binding.badgeOneInitial, "Complete one pomodoro cycle");
        TooltipCompat.setTooltipText(binding.badgeTwoInitial, "Complete two pomodoro cycle");
        TooltipCompat.setTooltipText(binding.badgeTwoInitial, "Complete three pomodoro cycle");
        TooltipCompat.setTooltipText(binding.badgeFourInitial, "Create an account!");
    }

    private void updateBadges() {
        if (pomodoroCycles >= 10) {
            binding.badgeThreeInitial.setAlpha((float) 1.00);
        }

        if (pomodoroCycles >= 5) {
            binding.badgeTwoInitial.setAlpha((float) 1.00);
        }

        if (pomodoroCycles >= 1) {
            binding.badgeOneInitial.setAlpha((float) 1.00);
        }

        if (!TextUtils.isEmpty(UserAccount.uid)) {
            binding.badgeFourInitial.setAlpha((float) 1.00);
        }
    }

    private void updateStats() {
        String workTotalString = workTotal + "s";
        String breakTotalString = breakTotal + "s";

        binding.pomodoroTotal.setText(String.valueOf(pomodoroTotal));
        binding.workTotal.setText(workTotalString);
        binding.breakTotal.setText(breakTotalString);
    }
}

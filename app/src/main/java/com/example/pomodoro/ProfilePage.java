package com.example.pomodoro;

import static com.example.pomodoro.UserAccount.breakTotal;
import static com.example.pomodoro.UserAccount.pomodoroCycles;
import static com.example.pomodoro.UserAccount.pomodoroTotal;
import static com.example.pomodoro.UserAccount.workTotal;

import android.net.Uri;
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

    private Uri currentUri;

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
        updateInventory();
        showToolTips();

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statsLayout.setVisibility(View.GONE);
                binding.badgeLayout.setVisibility(View.GONE);
                binding.editButton.setVisibility(View.GONE);
                binding.saveAvatarButton.setVisibility(View.VISIBLE);
                binding.maskLayout.setClickable(false);
            }
        });

        binding.saveAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statsLayout.setVisibility(View.VISIBLE);
                binding.badgeLayout.setVisibility(View.VISIBLE);
                binding.editButton.setVisibility(View.VISIBLE);
                binding.saveAvatarButton.setVisibility(View.GONE);
                binding.maskLayout.setClickable(true);
            }
        });

        binding.commonOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/common_one");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.rareOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/rare_one");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.epicOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/epic_one");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });
    }

    private void updateInventory() {
        if (UserAccount.isCommonOne()) {
            binding.commonOneImage.setAlpha(1f);
        }

        if (UserAccount.isRareOne()) {
            binding.rareOneImage.setAlpha(1f);
        }

        if (UserAccount.isEpicOne()) {
            binding.epicOneImage.setAlpha(1f);
        }
    }

    private void updateProfile() {
        binding.iconProfileImage.setImageURI(UserAccount.getUriImage());
        binding.nameProfileText.setText(UserAccount.getUsername());
        binding.tomatoesUserText.setText(String.valueOf(UserAccount.getTomatoes()));

        currentUri = UserAccount.getUriImage();
    }

    private void showToolTips() {
        TooltipCompat.setTooltipText(binding.badgeOneInitial, "Complete one pomodoro cycle");
        TooltipCompat.setTooltipText(binding.badgeTwoInitial, "Complete two pomodoro cycle");
        TooltipCompat.setTooltipText(binding.badgeTwoInitial, "Complete three pomodoro cycle");
        TooltipCompat.setTooltipText(binding.badgeFourInitial, "Create an account!");
    }

    private void updateBadges() {
        if (pomodoroCycles >= 10) {
            binding.badgeThreeInitial.setAlpha(1.00f);
        }

        if (pomodoroCycles >= 5) {
            binding.badgeTwoInitial.setAlpha(1.00f);
        }

        if (pomodoroCycles >= 1) {
            binding.badgeOneInitial.setAlpha(1.00f);
        }

        if (!TextUtils.isEmpty(UserAccount.uid)) {
            binding.badgeFourInitial.setAlpha(1.00f);
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

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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.databinding.FragmentProfilePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;

public class ProfilePageFragment extends Fragment {

    private FragmentProfilePageBinding binding;

    private Uri currentUri;
    private String currentBackground = "";

    class DatabaseThread extends Thread {

        DatabaseThread() {
        }

        public void run() {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(currentUri).build();
            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
            if (!currentBackground.isEmpty()) {
                UserAccount.updateFirestore("bgUri", String.valueOf(currentBackground));
                currentBackground = "";
            }
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentProfilePageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statsLayout.setVisibility(View.GONE);
                binding.badgeLayout.setVisibility(View.GONE);
                binding.editButton.setVisibility(View.GONE);
                binding.saveAvatarButton.setVisibility(View.VISIBLE);
                binding.editBackground.setVisibility(View.GONE);
                binding.maskLayout.setClickable(false);
                binding.maskBackgroundLayout.setClickable(false);
            }
        });

        binding.saveAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statsLayout.setVisibility(View.VISIBLE);
                binding.badgeLayout.setVisibility(View.VISIBLE);
                binding.editButton.setVisibility(View.VISIBLE);
                binding.editBackground.setVisibility(View.VISIBLE);
                binding.saveAvatarButton.setVisibility(View.GONE);
                binding.maskLayout.setClickable(true);
                binding.maskBackgroundLayout.setClickable(true);

                if (!currentBackground.isEmpty()) {
                    UserAccount.setUriBackground(Uri.parse(currentBackground));
                    ((MainActivity) requireActivity()).updateBackground();

                    String currentBg = ShopFragment.getFileName(currentBackground);
                    ((MainActivity)requireActivity()).checkDarkMode(currentBg);
                }
                UserAccount.setUriImage(currentUri);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    DatabaseThread dThread = new DatabaseThread();
                    dThread.start();
                }
            }
        });

        binding.commonOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/common_one");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.commonTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/common_two");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.commonThreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/common_three");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.commonFourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/common_four");
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

        binding.rareTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/rare_two");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.rareThreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/rare_three");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.rareFourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/rare_four");
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

        binding.epicTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/epic_two");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.epicThreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/epic_three");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.backgroundOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBackground = "android.resource://com.example.pomodoro/drawable/background_cyan";
                binding.profilePageLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.background_cyan));
            }
        });

        binding.backgroundTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBackground = "android.resource://com.example.pomodoro/drawable/background_blue";
                binding.profilePageLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.background_blue));
            }
        });

        binding.backgroundThreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBackground = "android.resource://com.example.pomodoro/drawable/background_purple";
                binding.profilePageLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.background_purple));
            }
        });

        binding.backgroundFourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBackground = "android.resource://com.example.pomodoro/drawable/background_dark";
                binding.profilePageLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.background_dark));
            }
        });

        binding.legendaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUri = Uri.parse("android.resource://com.example.pomodoro/drawable/legendary");
                binding.iconProfileImage.setImageURI(currentUri);
            }
        });

        binding.profilePageLayout.setBackground(null);

        updateProfile();
        updateStats();
        updateBadges();
        updateInventory();
        showToolTips();
    }

    private void updateInventory() {
        checkHasAvatar(binding.commonOneImage, UserAccount.isCommonOne());
        checkHasAvatar(binding.commonTwoImage, UserAccount.isCommonTwo());
        checkHasAvatar(binding.commonThreeImage, UserAccount.isCommonTwo());
        checkHasAvatar(binding.commonFourImage, UserAccount.isCommonFour());
        checkHasAvatar(binding.rareOneImage, UserAccount.isRareOne());
        checkHasAvatar(binding.rareTwoImage, UserAccount.isRareTwo());
        checkHasAvatar(binding.rareThreeImage, UserAccount.isRareThree());
        checkHasAvatar(binding.rareFourImage, UserAccount.isRareFour());
        checkHasAvatar(binding.epicOneImage, UserAccount.isEpicOne());
        checkHasAvatar(binding.epicTwoImage, UserAccount.isEpicTwo());
        checkHasAvatar(binding.epicThreeImage, UserAccount.isEpicThree());
        checkHasAvatar(binding.backgroundOneImage, UserAccount.getIsBackgroundCyan());
        checkHasAvatar(binding.backgroundTwoImage, UserAccount.getIsBackgroundBlue());
        checkHasAvatar(binding.backgroundThreeImage, UserAccount.getIsBackgroundPurple());
        checkHasAvatar(binding.backgroundFourImage, UserAccount.getIsBackgroundDark());
        checkHasAvatar(binding.legendaryImage, UserAccount.isLegendary());
    }

    /**
     * Checks if the user has already unlocked the avatar
     * @param avatarImage - the avatar being checked
     * @param isUnlocked - boolean variable if unlocked
     */
    private void checkHasAvatar(ImageView avatarImage, boolean isUnlocked) {
        if (isUnlocked) {
            avatarImage.setAlpha(1f);
        }
        avatarImage.setClickable(isUnlocked);
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
        TooltipCompat.setTooltipText(binding.badgeFiveInitial, "Try out and create your own custom session");
        TooltipCompat.setTooltipText(binding.badgeSixInitial, "Unlock the rarest legendary icon");
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

        if (UserAccount.getCustomWorkTwo() != 0) {
            binding.badgeFiveInitial.setAlpha(1.00f);
        }
    }

    private void updateStats() {
        String workTotalString = workTotal + "s";
        String breakTotalString = breakTotal + "s";

        binding.pomodoroTotal.setText(String.valueOf(pomodoroTotal));
        binding.workTotal.setText(workTotalString);
        binding.breakTotal.setText(breakTotalString);
        binding.pomodoroCycle.setText(String.valueOf(pomodoroCycles));
    }
}

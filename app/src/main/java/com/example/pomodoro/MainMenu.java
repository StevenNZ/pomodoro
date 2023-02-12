package com.example.pomodoro;

import static com.example.pomodoro.LoginPage.databaseReference;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro.databinding.MainMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class MainMenu extends Fragment {

    private MainMenuBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = MainMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenu.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenu.this)
                        .navigate(R.id.action_mainMenu_to_statisticsPage);
            }
        });

        binding.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainMenu.this).navigate(R.id.action_mainMenu_to_loginPage);
            }
        });

        Animation showLayout = AnimationUtils.loadAnimation(requireContext(), R.anim.show_layout);
        Animation hideLayout = AnimationUtils.loadAnimation(requireContext(), R.anim.hide_layout);
        binding.mainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.profileLayout.getVisibility() == View.GONE) {
                    binding.profileLayout.setVisibility(View.VISIBLE);
                    binding.userManageLayout.setVisibility(View.VISIBLE);

                    binding.profileLayout.startAnimation(showLayout);
                    binding.userManageLayout.startAnimation(showLayout);
                } else {
                    binding.userManageLayout.setVisibility(View.GONE);
                    binding.profileLayout.setVisibility(View.GONE);

                    binding.profileLayout.startAnimation(hideLayout);
                    binding.userManageLayout.startAnimation(hideLayout);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("dataFromLP", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                updateUserProfile();
            }
        });

        if (auth.getCurrentUser() != null) {
            if (UserAccount.emailAddress.isEmpty()) {
                updateUserAccount();
            }
            updateUserProfile();
        }
    }

    private void updateUserAccount() {
        String uid = auth.getUid();
        databaseReference.child("Users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();

                UserAccount.setUID(uid);
                LoginPage.retrieveUserInfo(snapshot);
                LoginPage.retrieveUserStats(snapshot);
                LoginPage.retrieveUserCustom(snapshot);
                LoginPage.retrieveUserInventory(snapshot);

                binding.tomatoesMenuText.setText(String.valueOf(UserAccount.getTomatoes()));
            }
        });
    }

    private void updateUserProfile() {
        binding.usernameText.setText(auth.getCurrentUser().getDisplayName());
        binding.mainIcon.setImageURI(auth.getCurrentUser().getPhotoUrl());
        binding.tomatoesMenuText.setText(String.valueOf(UserAccount.getTomatoes()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
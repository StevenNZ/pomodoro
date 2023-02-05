package com.example.pomodoro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro.databinding.MainMenuBinding;

public class MainMenu extends Fragment {

    private MainMenuBinding binding;
    private static boolean isUser;

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

        binding.statsIcon.setOnClickListener(new View.OnClickListener() {
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

        getParentFragmentManager().setFragmentResultListener("dataFromLP", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                isUser = true;
                String data = result.getString("lpUsername");
                updateUsername(data);
            }
        });

        if (isUser) {
            updateUsername(UserAccount.getUsername());
        }
    }

    private void updateUsername(String username) {
        binding.usernameText.setText(username);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
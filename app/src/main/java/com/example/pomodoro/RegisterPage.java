package com.example.pomodoro;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.databinding.RegisterPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends Fragment {

    private RegisterPageBinding binding;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RegisterPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        binding.btnSignUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = binding.emailText.getText().toString();
                String username = binding.usernameSignUpText.getText().toString();
                String password = binding.passwordSIgnUpText.getText().toString();

                if (checkIfEmpty(emailAddress, username, password)) {
                    Toast.makeText(requireContext(), "Make sure fields are not empty", Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 3) {
                    Toast.makeText(requireContext(), "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(emailAddress, username, password);
                }
            }
        });
    }

    private void registerUser(String emailAddress, String username, String password) {

        auth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Your user has been registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkIfEmpty(String emailAddress, String username, String password) {
        return TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password);
    }
}

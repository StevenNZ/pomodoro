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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends Fragment {

    private RegisterPageBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

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

        binding.btnSignUpRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = binding.emailText.getText().toString();
                String username = binding.usernameSignUpText.getText().toString();
                String password = binding.passwordSIgnUpText.getText().toString();
                String passwordTwo = binding.passwordTwoSIgnUpText.getText().toString();

                if (checkIfEmpty(emailAddress, username, password)) {
                    Toast.makeText(requireContext(), "Make sure fields are not empty", Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 3) {
                    Toast.makeText(requireContext(), "Password too short", Toast.LENGTH_SHORT).show();
                } else if (!checkIfMatch(password, passwordTwo)) {
                    Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_SHORT).show();
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
                    // create object of DatabaseReference class which gives access to firebase realtime database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                    // grabbing the user's UID as our unique identity
                    FirebaseUser user = auth.getCurrentUser();
                    String uid = user.getUid();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

                    user.updateProfile(profileUpdates);

                    // sending to the database
                    updateDatabase(databaseReference.child(uid), emailAddress, username, password);

                    sendVerifyEmail();
                } else {
                    Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendVerifyEmail() {
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Registered Successfully! Please check your email for verification", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                auth.signOut();
                requireActivity().onBackPressed();
            }
        });
    }

    private void updateDatabase(DatabaseReference databaseReference, String emailAddress, String username, String password) {
        String statPath = "Statistics";
        String customPath = "Custom";

        databaseReference.child("Email Address").setValue(emailAddress);
        databaseReference.child("Username").setValue(username);
        databaseReference.child("Password").setValue(password);

        updateStatsDatabase(databaseReference.child(statPath));
        updateCustomDatabase(databaseReference.child(customPath));
    }

    private void updateCustomDatabase(DatabaseReference databaseReference) {
        databaseReference.child("Title One").setValue("Pomodoro");
        databaseReference.child("Title Two").setValue("Title");
        databaseReference.child("Work One").setValue(25);
        databaseReference.child("Work Two").setValue(0);
        databaseReference.child("Short One").setValue(5);
        databaseReference.child("Short Two").setValue(0);
        databaseReference.child("Long One").setValue(15);
        databaseReference.child("Long Two").setValue(0);
    }

    private void updateStatsDatabase(DatabaseReference databaseReference) {
        databaseReference.child("Work Total").setValue(0);
        databaseReference.child("Break Total").setValue(0);
        databaseReference.child("Pomodoro Total").setValue(0);
        databaseReference.child("Pomodoro Cycle Total").setValue(0);
    }

    private boolean checkIfEmpty(String emailAddress, String username, String password) {
        return TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password);
    }

    private boolean checkIfMatch(String password, String passwordTwo) {
        return password.equals(passwordTwo);
    }
}

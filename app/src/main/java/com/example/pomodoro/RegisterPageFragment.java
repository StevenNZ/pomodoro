package com.example.pomodoro;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pomodoro.databinding.FragmentRegisterPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPageFragment extends Fragment {

    private FragmentRegisterPageBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Confetti confetti;

    private int avatarSelected = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterPageBinding.inflate(inflater, container, false);
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

                if (avatarSelected == 0) {
                    Toast.makeText(requireContext(), "Make sure to select an avatar", Toast.LENGTH_SHORT).show();
                } else if (checkIfEmpty(emailAddress, username, password)) {
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

        binding.avatarOneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarSelected = 1;
                binding.avatarOneImage.setAlpha((float) 1.00);
                binding.avatarTwoImage.setAlpha((float) 0.5);
            }
        });

        binding.avatarTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarSelected = 2;
                binding.avatarOneImage.setAlpha((float) 0.5);
                binding.avatarTwoImage.setAlpha((float) 1.00);
            }
        });

        confetti = new Confetti(binding.konfettiViewRegister, getContext());
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

                    updateUserInfo(user, username);

                    // sending to the database
                    updateDatabase(databaseReference.child(uid), emailAddress, username);
                    updateFirestore(emailAddress, uid, username);

                    sendVerifyEmail();
                } else {
                    Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateFirestore(String emailAddress, String uid, String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("uid", uid);
        userInfo.put("username", username);
        userInfo.put("bgOne", false);
        userInfo.put("bgTwo", false);
        userInfo.put("bgThree", false);
        userInfo.put("bgFour", false);

        db.collection("Users").document(emailAddress)
                .set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                System.out.println("Success");
            }
        });
    }

    private void updateUserInfo(FirebaseUser user, String username) {
        String uriString = "android.resource://com.example.pomodoro/drawable/";

        if (avatarSelected == 1) {
            uriString = uriString + "start_avatar_one";
        } else {
            uriString = uriString + "start_avatar_two";
        }
        Uri uri = Uri.parse(uriString);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).setPhotoUri(uri).build();

        user.updateProfile(profileUpdates);
    }

    private void sendVerifyEmail() {
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Registered Successfully! Please check your email for verification", Toast.LENGTH_LONG).show();
                    confetti.parade();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                auth.signOut();
                UserAccount.resetGuest();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void updateDatabase(DatabaseReference databaseReference, String emailAddress, String username) {
        String statPath = "Statistics";
        String customPath = "Custom";
        String inventoryPath = "Inventory";

        databaseReference.child("Email Address").setValue(emailAddress);
        databaseReference.child("Username").setValue(username);

        updateStatsDatabase(databaseReference.child(statPath));
        updateCustomDatabase(databaseReference.child(customPath));
        updateInventoryDatabase(databaseReference.child(inventoryPath));
    }

    private void updateInventoryDatabase(DatabaseReference databaseReference) {
        databaseReference.child("Tomatoes").setValue(0);
        databaseReference.child("Common One").setValue(false);
        databaseReference.child("Common Two").setValue(false);
        databaseReference.child("Common Three").setValue(false);
        databaseReference.child("Common Four").setValue(false);
        databaseReference.child("Rare One").setValue(false);
        databaseReference.child("Rare Two").setValue(false);
        databaseReference.child("Rare Three").setValue(false);
        databaseReference.child("Rare Four").setValue(false);
        databaseReference.child("Epic One").setValue(false);
        databaseReference.child("Epic Two").setValue(false);
        databaseReference.child("Epic Three").setValue(false);
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

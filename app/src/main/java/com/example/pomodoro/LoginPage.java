package com.example.pomodoro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro.databinding.LoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPage extends Fragment {

    protected static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pomodoro-2bd96-default-rtdb.firebaseio.com/");

    private LoginPageBinding binding;

    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void updateUserAccount() {
        String uid = auth.getUid();
        databaseReference.child("Users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();

                UserAccount.setUID(uid);
                retrieveUserInfo(snapshot);
                retrieveUserStats(snapshot);
                retrieveUserCustom(snapshot);
            }
        });
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = LoginPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginPage.this).navigate(R.id.action_loginPage_to_registerPage);
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailLoginText.getText().toString();
                String password = binding.passwordText.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(requireContext(), "Make sure fields are not empty", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        binding.forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginPage.this).navigate(R.id.action_loginPage_to_forgotPassword);
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (auth.getCurrentUser().isEmailVerified()) {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();

                    String uid = authResult.getUser().getUid();
                    DatabaseReference databaseUsers = databaseReference.child("Users").child(uid);
                    databaseUsers.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            // retrieve data from firebase
                            retrieveData(task);

                            updateMainMenu();

                            requireActivity().onBackPressed();
                        }

                        private void retrieveData(Task<DataSnapshot> task) {
                            DataSnapshot dataSnapshot = task.getResult();

                            UserAccount.setUID(uid);
                            retrieveUserInfo(dataSnapshot);
                            retrieveUserStats(dataSnapshot);
                            retrieveUserCustom(dataSnapshot);
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                auth.signOut();
            }
        });
    }

    private static void retrieveUserCustom(DataSnapshot dataSnapshot) {
        DataSnapshot customSnapshot = dataSnapshot.child("Custom");

        String titleOne = (String) customSnapshot.child("Title One").getValue();
        String titleTwo = (String) customSnapshot.child("Title Two").getValue();
        int workOne = Integer.parseInt(String.valueOf(customSnapshot.child("Work One").getValue()));
        int workTwo = Integer.parseInt(String.valueOf(customSnapshot.child("Work Two").getValue()));
        int shortOne = Integer.parseInt(String.valueOf(customSnapshot.child("Short One").getValue()));
        int shortTwo = Integer.parseInt(String.valueOf(customSnapshot.child("Short Two").getValue()));
        int longOne = Integer.parseInt(String.valueOf(customSnapshot.child("Long One").getValue()));
        int longTwo = Integer.parseInt(String.valueOf(customSnapshot.child("Long Two").getValue()));

        UserAccount.setCustomTitleOne(titleOne);
        UserAccount.setCustomTitleTwo(titleTwo);
        UserAccount.setCustomWorkOne(workOne);
        UserAccount.setCustomWorkTwo(workTwo);
        UserAccount.setCustomShortOne(shortOne);
        UserAccount.setCustomShortTwo(shortTwo);
        UserAccount.setCustomLongOne(longOne);
        UserAccount.setCustomLongTwo(longTwo);
    }

    private static void retrieveUserStats(DataSnapshot dataSnapshot) {
        DataSnapshot statsSnapshot = dataSnapshot.child("Statistics");

        int pomodoroTotal = Integer.parseInt(String.valueOf(statsSnapshot.child("Pomodoro Total").getValue()));
        int workTotal = Integer.parseInt(String.valueOf(statsSnapshot.child("Work Total").getValue()));
        int breakTotal = Integer.parseInt(String.valueOf(statsSnapshot.child("Break Total").getValue()));
        int cycleTotal = Integer.parseInt(String.valueOf(statsSnapshot.child("Pomodoro Cycle Total").getValue()));

        UserAccount.setPomodoroTotal(pomodoroTotal);
        UserAccount.setWorkTotal(workTotal);
        UserAccount.setBreakTotal(breakTotal);
        UserAccount.setPomodoroCycles(cycleTotal);
    }

    private static void retrieveUserInfo(DataSnapshot dataSnapshot) {
        UserAccount.setEmailAddress(String.valueOf(dataSnapshot.child("Email Address").getValue()));
        UserAccount.setUsername(String.valueOf(dataSnapshot.child("Username").getValue()));
        UserAccount.setPassword(String.valueOf(dataSnapshot.child("Password").getValue()));
    }

    private void updateMainMenu() {
        Bundle result = new Bundle();
        result.putString("lpUsername", UserAccount.getUsername());
        getParentFragmentManager().setFragmentResult("dataFromLP", result);
    }
}

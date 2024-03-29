package com.example.pomodoro;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro.databinding.FragmentLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPageFragment extends Fragment {

    protected static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pomodoro-2bd96-default-rtdb.firebaseio.com/");
    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentLoginPageBinding binding;

    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    class DatabaseThread extends Thread {

        private final Task<DataSnapshot> task;
        private final String uid;
        private final String email;

        DatabaseThread(Task<DataSnapshot> task, String uid, String email) {
            this.task = task;
            this.uid = uid;
            this.email = email;
        }

        public void run() {
            DataSnapshot dataSnapshot = task.getResult();

            UserAccount.setUID(uid);
            retrieveUserInfo(dataSnapshot);
            retrieveUserStats(dataSnapshot);
            retrieveUserCustom(dataSnapshot);
            retrieveUserInventory(dataSnapshot);
            retrieveFirestore(email);
        }
    }

    class FirestoreThread extends Thread {

        private final Task<DocumentSnapshot> task;

        FirestoreThread(Task<DocumentSnapshot> task) {
            this.task = task;
        }

        public void run() {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    retrieveBackground(document);
                }
            }
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginPageFragment.this).navigate(R.id.action_loginPage_to_registerPage);
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
                NavHostFragment.findNavController(LoginPageFragment.this).navigate(R.id.action_loginPage_to_forgotPassword);
            }
        });
        updateCurrentUserText();

        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() == null) {
                    Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signOut();
                    UserAccount.resetGuest();
                    updateCurrentUserText();
                    ((MainActivity)requireActivity()).updateBackground();
                    ((MainActivity)requireActivity()).checkDarkMode("");
                }
            }
        });
        // Animations for user fab
        Animation showLayout = AnimationUtils.loadAnimation(getContext(), R.anim.show_layout);
        Animation hideLayout = AnimationUtils.loadAnimation(getContext(), R.anim.hide_layout);

        binding.mainLoginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.registerLayout.getVisibility() == View.GONE) {
                    binding.registerLayout.setVisibility(View.VISIBLE);
                    binding.logOutLayout.setVisibility(View.VISIBLE);
                    binding.passwordLayout.setVisibility(View.VISIBLE);

                    binding.registerLayout.startAnimation(showLayout);
                    binding.logOutLayout.startAnimation(showLayout);
                    binding.passwordLayout.startAnimation(showLayout);
                } else {
                    binding.registerLayout.setVisibility(View.GONE);
                    binding.logOutLayout.setVisibility(View.GONE);
                    binding.passwordLayout.setVisibility(View.GONE);

                    binding.registerLayout.startAnimation(hideLayout);
                    binding.logOutLayout.startAnimation(hideLayout);
                    binding.passwordLayout.startAnimation(hideLayout);
                }
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
                            // retrieve data from firebase through a thread
                            DatabaseThread databaseThread = new DatabaseThread(task, uid, email);
                            databaseThread.start();

                            updateMainMenu();

                            requireActivity().getOnBackPressedDispatcher().onBackPressed();
                        }
                    });

                    db.collection("Users").document(auth.getCurrentUser().getEmail()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    FirestoreThread fsThread = new FirestoreThread(task);
                                    fsThread.start();
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
            }
        });
    }

    protected static void retrieveBackground(DocumentSnapshot document) {
        UserAccount.setUriBackground(Uri.parse(String.valueOf(document.get("bgUri"))));
        UserAccount.setIsBackgroundCyan(Boolean.parseBoolean(String.valueOf(document.get("bgOne"))));
        UserAccount.setIsBackgroundBlue(Boolean.parseBoolean(String.valueOf(document.get("bgTwo"))));
        UserAccount.setIsBackgroundPurple(Boolean.parseBoolean(String.valueOf(document.get("bgThree"))));
        UserAccount.setIsBackgroundDark(Boolean.parseBoolean(String.valueOf(document.get("bgFour"))));
    }

    protected static void retrieveFirestore(String email) {
        db.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        retrieveBackground(document);
                    }
                }
            }
        });
    }

    /**
     * Updates the current user text at the bottom as well as the user picture
     */
    private void updateCurrentUserText() {
        String currentUserText = "Currently no user logged in";
        if (auth.getCurrentUser() != null) {
            currentUserText = "Current user is " + auth.getCurrentUser().getDisplayName();
        }
        binding.userLoginText.setText(currentUserText);
        binding.mainLoginIcon.setImageURI(UserAccount.getUriImage());
    }

    /**
     * Retrieve's the user's data from firebase in this case it's inventory
     *
     * @param dataSnapshot - a snapshot of the user's database state
     */
    protected static void retrieveUserInventory(DataSnapshot dataSnapshot) {
        DataSnapshot inventorySnapShot = dataSnapshot.child("Inventory");

        int tomatoes = Integer.parseInt(String.valueOf(inventorySnapShot.child("Tomatoes").getValue()));
        boolean commonOne = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Common One").getValue()));
        boolean commonTwo = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Common Two").getValue()));
        boolean commonThree = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Common Three").getValue()));
        boolean commonFour = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Common Four").getValue()));
        boolean rareOne = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Rare One").getValue()));
        boolean rareTwo = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Rare Two").getValue()));
        boolean rareThree = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Rare Three").getValue()));
        boolean rareFour = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Rare Four").getValue()));
        boolean epicOne = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Epic One").getValue()));
        boolean epicTwo = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Epic Two").getValue()));
        boolean epicThree = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Epic Three").getValue()));
        boolean legendary = Boolean.parseBoolean(String.valueOf(inventorySnapShot.child("Legendary").getValue()));

        UserAccount.setTomatoes(tomatoes);
        UserAccount.setCommonOne(commonOne);
        UserAccount.setCommonTwo(commonTwo);
        UserAccount.setCommonThree(commonThree);
        UserAccount.setCommonFour(commonFour);
        UserAccount.setRareOne(rareOne);
        UserAccount.setRareTwo(rareTwo);
        UserAccount.setRareThree(rareThree);
        UserAccount.setRareFour(rareFour);
        UserAccount.setEpicOne(epicOne);
        UserAccount.setEpicTwo(epicTwo);
        UserAccount.setEpicThree(epicThree);
        UserAccount.setLegendary(legendary);
    }

    protected static void retrieveUserCustom(DataSnapshot dataSnapshot) {
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

    protected static void retrieveUserStats(DataSnapshot dataSnapshot) {
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

    protected static void retrieveUserInfo(DataSnapshot dataSnapshot) {
        UserAccount.setEmailAddress(String.valueOf(dataSnapshot.child("Email Address").getValue()));
        UserAccount.setUsername(String.valueOf(dataSnapshot.child("Username").getValue()));
        UserAccount.setPassword(String.valueOf(dataSnapshot.child("Password").getValue()));
        UserAccount.setUriImage(auth.getCurrentUser().getPhotoUrl());
    }

    /**
     * Updates the main menu when transitioning after log in
     */
    private void updateMainMenu() {
        Bundle result = new Bundle();
        result.putString("lpUsername", UserAccount.getUsername());
        getParentFragmentManager().setFragmentResult("dataFromLP", result);
    }
}

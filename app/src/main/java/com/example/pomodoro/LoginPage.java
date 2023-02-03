package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import org.w3c.dom.Text;

public class LoginPage extends Fragment {

    private LoginPageBinding binding;

    private FirebaseAuth auth;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pomodoro-2bd96-default-rtdb.firebaseio.com/");

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

        auth = FirebaseAuth.getInstance();

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
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();

                String uid = authResult.getUser().getUid();
                DatabaseReference databaseUsers = databaseReference.child("Users").child(uid);
                databaseUsers.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        UserAccount.setEmailAddress(String.valueOf(dataSnapshot.child("Email Address").getValue()));
                        UserAccount.setUsername(String.valueOf(dataSnapshot.child("Username").getValue()));
                        UserAccount.setPassword(String.valueOf(dataSnapshot.child("Password").getValue()));

                        Bundle result = new Bundle();
                        result.putString("lpUsername", UserAccount.getUsername());
                        getParentFragmentManager().setFragmentResult("dataFromLP", result);

                        requireActivity().onBackPressed();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

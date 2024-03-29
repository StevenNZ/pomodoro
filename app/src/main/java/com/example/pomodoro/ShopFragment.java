package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.pomodoro.databinding.FragmentShopBinding;
import com.google.firebase.firestore.auth.User;

import java.util.Random;


public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;

    private Confetti confetti;
    private Animation initAnim;

    private float swipeY1;
    private float swipeY2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAnim = AnimationUtils.loadAnimation(getContext(), R.anim.wiggle);
        binding.explodeImage.startAnimation(initAnim);

        binding.explodeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        // when user holds down
                        swipeY1 = event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // when user releases
                        swipeY2 = event.getY();

                        if (swipeY2 < swipeY1) {
                            int tomatoes = UserAccount.getTomatoes();
                            if (tomatoes < 200) {
                                Toast.makeText(getContext(), "Not enough tomatoes :(", Toast.LENGTH_SHORT).show();
                            } else {
                                updateTomatoes(tomatoes);
                                layoutUpdate();
                                itemUpdate();
                            }
                        }
                        return true;
                }
                return false;
            }
        });

        binding.unlockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.shopLayout.setAlpha(1f);
                binding.unlockLayout.setVisibility(View.GONE);
            }
        });

        binding.tomatoesShopText.setText(String.valueOf(UserAccount.getTomatoes()));
        confetti = new Confetti(binding.konfettiView, getContext());
    }

    private void updateTomatoes(int tomatoes) {
        UserAccount.setTomatoes(tomatoes - 200);

        String tomatoesText = String.valueOf(UserAccount.getTomatoes());
        binding.tomatoesShopText.setText(tomatoesText);
    }

    private void itemUpdate() {
        float randFloat = new Random().nextFloat();
        String itemImage;
        String tier;

        if (randFloat >= 0.99) {
            itemImage = "android.resource://com.example.pomodoro/drawable/legendary";
            UserAccount.setLegendary(true);
            tier = "Legendary";
            confetti.explode();
            confetti.parade();
            confetti.rain();
        } else if (randFloat >= 0.95f) {
            itemImage = getRandomEpic();
            tier = "Epic";
            confetti.explode();
            confetti.parade();
        } else if (randFloat >= 0.80f) {
            itemImage = getRandomRare();
            tier = "Rare";
            confetti.explode();
        } else {
            itemImage = getRandomCommon();
            tier = "Common";
        }
        String key = getFileName(itemImage);
        UserAccount.updateDatabase("Inventory", key, true);
        binding.unlockImage.setImageURI(Uri.parse(itemImage));
        binding.unlockText.setText(tier);
    }

    private String getRandomCommon() {
        float randFloat = new Random().nextFloat();
        String commonName;

        if (randFloat < 0.25f) {
            commonName = "android.resource://com.example.pomodoro/drawable/common_one";
            UserAccount.setCommonOne(true);
        } else if (randFloat < 0.5f) {
            commonName = "android.resource://com.example.pomodoro/drawable/common_two";
            UserAccount.setCommonTwo(true);
        } else if (randFloat < 0.75) {
            commonName = "android.resource://com.example.pomodoro/drawable/common_three";
            UserAccount.setCommonThree(true);
        } else {
            commonName = "android.resource://com.example.pomodoro/drawable/common_four";
            UserAccount.setCommonFour(true);
        }

        return commonName;
    }

    private String getRandomRare() {
        float randFloat = new Random().nextFloat();
        float itemTotal = 7f;
        String rareName;

        if (randFloat < 1/itemTotal) {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_one";
            UserAccount.setRareOne(true);
        } else if (randFloat < 2/itemTotal) {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_two";
            UserAccount.setRareTwo(true);
        } else if (randFloat < 3/itemTotal) {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_three";
            UserAccount.setRareThree(true);
        } else if (randFloat < 4/itemTotal){
            rareName = "android.resource://com.example.pomodoro/drawable/rare_four";
            UserAccount.setRareFour(true);
        } else if (randFloat < 5/itemTotal){
            rareName = "android.resource://com.example.pomodoro/drawable/background_cyan";
            UserAccount.setIsBackgroundCyan(true);
            UserAccount.updateFirestore("bgOne", true);
        } else if (randFloat < 6/itemTotal){
            rareName = "android.resource://com.example.pomodoro/drawable/background_blue";
            UserAccount.setIsBackgroundBlue(true);
            UserAccount.updateFirestore("bgTwo", true);
        } else {
            rareName = "android.resource://com.example.pomodoro/drawable/background_purple";
            UserAccount.setIsBackgroundPurple(true);
            UserAccount.updateFirestore("bgThree", true);
        }

        return rareName;
    }

    private String getRandomEpic() {
        float randFloat = new Random().nextFloat();
        float epicTotal = 4;
        String epicName;

        if (randFloat < 1/epicTotal) {
            epicName = "android.resource://com.example.pomodoro/drawable/epic_one";
            UserAccount.setEpicOne(true);
        } else if (randFloat < 2/epicTotal) {
            epicName = "android.resource://com.example.pomodoro/drawable/epic_two";
            UserAccount.setEpicTwo(true);
        } else if (randFloat < 3/epicTotal){
            epicName = "android.resource://com.example.pomodoro/drawable/epic_three";
            UserAccount.setEpicThree(true);
        } else {
            epicName = "android.resource://com.example.pomodoro/drawable/background_dark";
            UserAccount.setIsBackgroundDark(true);
            UserAccount.updateFirestore("bgFour", true);
        }

        return epicName;
    }

    /**
     * Grabs the key of the inputted image to update the firebase database
     * @param itemImage - image of the file name needed
     * @return - String of the file name
     */
    protected static String getFileName(String itemImage) {
        StringBuilder output = new StringBuilder();
        String[] filenames = itemImage.split("/");
        String[] filenameSplit = filenames[filenames.length-1].split("_");
        for (String name : filenameSplit) {
            output.append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append(" ");
        }

        return output.deleteCharAt(output.length()-1).toString();
    }

    private void layoutUpdate() {
        binding.shopLayout.setAlpha(0.25f);
        binding.unlockLayout.setVisibility(View.VISIBLE);
    }
}
package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pomodoro.databinding.FragmentShopBinding;

import java.net.URI;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;


public class Shop extends Fragment {

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
                        swipeY1 = event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        swipeY2 = event.getY();

                        if (swipeY2 < swipeY1) {
                            if (UserAccount.getTomatoes() < 80) {
                                Toast.makeText(getContext(), "Not enough tomatoes :(", Toast.LENGTH_SHORT).show();
                            } else {
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

    private void itemUpdate() {
        float randFloat = new Random().nextFloat();
        String itemImage;
        String tier;

        if (randFloat >= 0.95f) {
            itemImage = getRandomEpic();
            tier = "Epic";
            confetti.explode();
            confetti.parade();
        } else if (randFloat >= 0.85f) {
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
        String rareName;

        if (randFloat < 0.25f) {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_one";
            UserAccount.setRareOne(true);
        } else if (randFloat < 0.5f) {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_two";
            UserAccount.setRareTwo(true);
        } else if (randFloat < 0.75) {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_three";
            UserAccount.setRareThree(true);
        } else {
            rareName = "android.resource://com.example.pomodoro/drawable/rare_four";
            UserAccount.setRareFour(true);
        }

        return rareName;
    }

    private String getRandomEpic() {
        float randFloat = new Random().nextFloat();
        String epicName;

        if (randFloat < 0.33f) {
            epicName = "android.resource://com.example.pomodoro/drawable/epic_one";
            UserAccount.setEpicOne(true);
        } else if (randFloat < 0.66f) {
            epicName = "android.resource://com.example.pomodoro/drawable/epic_two";
            UserAccount.setEpicTwo(true);
        } else {
            epicName = "android.resource://com.example.pomodoro/drawable/epic_three";
            UserAccount.setEpicThree(true);
        }

        return epicName;
    }

    private String getFileName(String itemImage) {
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
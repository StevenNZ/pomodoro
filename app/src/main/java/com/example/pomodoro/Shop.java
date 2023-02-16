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

    private Shape.DrawableShape drawableShape;
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

        final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.tomatoes_currency);
        drawableShape = new Shape.DrawableShape(drawable, true);

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
    }

    private void itemUpdate() {
        float randFloat = new Random().nextFloat();
        String itemImage;
        String tier;

        if (randFloat >= 0.99f) {
            UserAccount.setEpicOne(true);
            itemImage = "android.resource://com.example.pomodoro/drawable/epic_one";
            tier = "Epic";
            explode();
            parade();
        } else if (randFloat >= 0.9f) {
            UserAccount.setRareOne(true);
            itemImage = "android.resource://com.example.pomodoro/drawable/rare_one";
            tier = "Rare";
            explode();
        } else {
            UserAccount.setCommonOne(true);
            itemImage = "android.resource://com.example.pomodoro/drawable/common_one";
            tier = "Common";
        }
        rain();
        String key = getFileName(itemImage);
        UserAccount.updateDatabase("Inventory", key, true);
        binding.unlockImage.setImageURI(Uri.parse(itemImage));
        binding.unlockText.setText(tier);
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

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        binding.konfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build()
        );
    }

    public void parade() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
        binding.konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build()
        );
    }

    public void rain() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(100);
        binding.konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build()
        );
    }
}
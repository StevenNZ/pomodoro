package com.example.pomodoro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro.databinding.PomodoroTimerBinding;

import java.util.Locale;

public class PomodoroTimer extends Fragment {

    private static final long initialTime = 6000;
    private static final long shortBreakTime = 2000;
    private static final long longBreakTime = 4000;

    private boolean isRunning = false;
    private boolean isBreak = false;
    private int breakCount = 0;
    private long remainingTime = initialTime;
    private String workSession = "Study Session";

    private CountDownTimer timer;
    private PomodoroTimerBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = PomodoroTimerBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        updateTimer();

        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PomodoroTimer.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private void updateTimer() {
        int minutes = (int) (remainingTime/1000) / 60;
        int seconds = (int) (remainingTime/1000) % 60;

        String remainingTimeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.textTimer.setText(remainingTimeText);
        binding.textSession.setText(workSession);
    }

    private void startTimer() {
        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long timeUntilFinish) {
                remainingTime = timeUntilFinish;
                updateTimer();
            }

            @Override
            public void onFinish() {
                isRunning = false;

                if (isBreak) {// Break -> Work
                    remainingTime = initialTime;
                    workSession = "Study Session";
                    isBreak = false;
                    updateTimer();
                } else if (breakCount == 4) {// Work -> Long break
                    remainingTime = longBreakTime;
                    workSession = "Long Break";
                    isBreak = true;
                    breakCount = 0;
                    updateTimer();
                } else {// Work -> Short Break
                    remainingTime = shortBreakTime;
                    breakCount++;
                    workSession = "Short Break " + breakCount;
                    isBreak = true;
                    breakCount++;
                    updateTimer();
                }
            }
        }.start();

        isRunning = true;
    }

    private void pauseTimer() {
        timer.cancel();
        isRunning = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
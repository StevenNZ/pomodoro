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

    private static long workTime = 6000;
    private static long shortBreakTime = 2000;
    private static long longBreakTime = 4000;

    private boolean isRunning = false;
    private boolean isBreak = false;
    private int breakCount = 0;
    private long remainingTime = workTime;
    private String workSession = "Study Session";
    private long initialTime = workTime;

    private CountDownTimer timer;
    private PomodoroTimerBinding binding;

    public static void updateTimerSettings(long workTime, long shortTime, long longTime) {
        PomodoroTimer.workTime = workTime*60*1000;
        shortBreakTime = shortTime*60*1000;
        longBreakTime = longTime*60*1000;
    }

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
                    binding.buttonBack.setVisibility(View.VISIBLE);
                } else {
                    startTimer();
                    binding.buttonBack.setVisibility(View.INVISIBLE);
                }
            }
        });

        updateTimer();

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PomodoroTimer.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonPlay.setVisibility(View.VISIBLE);
                binding.buttonNewGame.setVisibility(View.INVISIBLE);
                remainingTime = workTime;
                workSession = "Study Session";
                isBreak = false;
                updateTimer();
                resetShortBreaks();
            }
        });
    }

    private void resetShortBreaks() {
        binding.tomatoOne.setVisibility(View.INVISIBLE);
        binding.tomatoTwo.setVisibility(View.INVISIBLE);
        binding.tomatoThree.setVisibility(View.INVISIBLE);
        binding.tomatoFour.setVisibility(View.INVISIBLE);
    }

    private void updateTimer() {
        int minutes = (int) (remainingTime / 1000) / 60;
        int seconds = (int) (remainingTime / 1000) % 60;

        String remainingTimeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.textTimer.setText(remainingTimeText);
        binding.textSession.setText(workSession);
        binding.progressBarTimer.setProgress((int) ((double)(remainingTime) / (double) (initialTime)*100));
        System.out.println((double)(remainingTime) / (double) (initialTime));
        System.out.println(remainingTime);
        System.out.println(initialTime);
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

                if (isBreak && breakCount == 0) {
                    binding.buttonPlay.setVisibility(View.INVISIBLE);
                    binding.buttonNewGame.setVisibility(View.VISIBLE);
                } else {
                    if (isBreak) {// Break -> Work
                        remainingTime = workTime;
                        workSession = "Study Session";
                        isBreak = false;
                    } else if (breakCount == 4) {// Work -> Long break
                        remainingTime = longBreakTime;
                        workSession = "Long Break";
                        isBreak = true;
                        breakCount = 0;
                    } else {// Work -> Short Break
                        remainingTime = shortBreakTime;
                        breakCount++;
                        workSession = "Short Break " + breakCount;
                        isBreak = true;
                        updateShortBreaks();
                    }
                    initialTime = remainingTime;
                    updateTimer();
                }
            }
        }.start();

        isRunning = true;
    }

    private void updateShortBreaks() {
        switch (breakCount) {
            case 1:
                binding.tomatoOne.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.tomatoTwo.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.tomatoThree.setVisibility(View.VISIBLE);
                break;
            case 4:
                binding.tomatoFour.setVisibility(View.VISIBLE);
                break;
        }
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
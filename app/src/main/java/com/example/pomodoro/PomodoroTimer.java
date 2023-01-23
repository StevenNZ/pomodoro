package com.example.pomodoro;

import android.os.Build;
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
    private int timeline = 0;
    private long remainingTime = workTime;
    private long initialTime = workTime;
    private long totalProgressTime = workTime*5 + shortBreakTime*4;
    private double cumulativeProgress = 0;
    private String workSession = "Study Session";

    private CountDownTimer timer;
    private PomodoroTimerBinding binding;

    public static void updateTimerSettings(long workTime, long shortTime, long longTime) {
//        PomodoroTimer.workTime = workTime*60*1000;
//        shortBreakTime = shortTime*60*1000;
//        longBreakTime = longTime*60*1000;
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
                    binding.workOne.setVisibility(View.VISIBLE);
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
                initialTime = workTime;
                workSession = "Study Session";
                isBreak = false;
                updateTimer();
                resetTimeline();
            }
        });
    }

    private void resetTimeline() {
        binding.workOne.setVisibility(View.INVISIBLE);
        binding.workTwo.setVisibility(View.INVISIBLE);
        binding.workThree.setVisibility(View.INVISIBLE);
        binding.workFour.setVisibility(View.INVISIBLE);
        binding.workFive.setVisibility(View.INVISIBLE);
        binding.breakOne.setVisibility(View.INVISIBLE);
        binding.breakTwo.setVisibility(View.INVISIBLE);
        binding.breakThree.setVisibility(View.INVISIBLE);
        binding.breakFour.setVisibility(View.INVISIBLE);
        binding.breakLong.setVisibility(View.INVISIBLE);
        binding.timelineProgress.setProgress(0);

        cumulativeProgress = 0;
    }

    private void updateTimer() {
        int minutes = (int) (remainingTime / 1000) / 60;
        int seconds = (int) (remainingTime / 1000) % 60;

        String remainingTimeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.textTimer.setText(remainingTimeText);
        binding.textSession.setText(workSession);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressBarTimer.setProgress((int) ((double)(remainingTime) / (double) (initialTime)*100), true);
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long timeUntilFinish) {
                remainingTime = timeUntilFinish;
                updateTimer();
                updateTimelineProgress();
            }

            @Override
            public void onFinish() {
                isRunning = false;

                if (isBreak && timeline == 0) {
                    binding.buttonPlay.setVisibility(View.INVISIBLE);
                    binding.buttonNewGame.setVisibility(View.VISIBLE);
                } else {
                    if (isBreak) {// Break -> Work
                        remainingTime = workTime;
                        workSession = "Study Session";
                        isBreak = false;
                        timeline++;
                    } else if (timeline == 8) {// Work -> Long break
                        remainingTime = longBreakTime;
                        workSession = "Long Break";
                        isBreak = true;
                        timeline = 0;
                    } else {// Work -> Short Break
                        remainingTime = shortBreakTime;
                        workSession = "Short Break";
                        isBreak = true;
                        timeline++;
                    }
                    initialTime = remainingTime;
                    updateTimelineIcons();
                    updateTimer();
                }
            }
        }.start();

        isRunning = true;
    }

    private void updateTimelineProgress() {
        double progressTotal = (double) initialTime/ (double) totalProgressTime;
        double progressPerSecond = progressTotal / ((double) initialTime / 1000.00);
        cumulativeProgress+=progressPerSecond;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.timelineProgress.setProgress((int) (cumulativeProgress*100), true);
        }
    }

    private void updateTimelineIcons() {
        switch (timeline) {
            case 1:
                binding.breakOne.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.workTwo.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.breakTwo.setVisibility(View.VISIBLE);
                break;
            case 4:
                binding.workThree.setVisibility(View.VISIBLE);
                break;
            case 5:
                binding.breakThree.setVisibility(View.VISIBLE);
                break;
            case 6:
                binding.workFour.setVisibility(View.VISIBLE);
                break;
            case 7:
                binding.breakFour.setVisibility(View.VISIBLE);
                break;
            case 8:
                binding.workFive.setVisibility(View.VISIBLE);
                break;
            case 0:
                binding.breakLong.setVisibility(View.VISIBLE);
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
package com.example.pomodoro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pomodoro.databinding.CustomSettingBinding;

public class CustomSetting extends Fragment {

    private CustomSettingBinding binding;
    private TextView currentTimeText;
    private EditText currentTitleText;

    private long workTime;
    private long shortBreakTime;
    private long longBreakTime;

    private boolean isEditing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CustomSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomTimes();

                if (!isEditing) {
                    updatePomodoro();

                    NavHostFragment.findNavController(CustomSetting.this)
                            .navigate(R.id.action_customSetting_to_pomodoro);
                } else {
                    saveCustomTimes();
                }
            }
        });

        binding.workBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.workText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.shortBreakBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.shortBreakText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.longBreakBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.longBreakText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.editButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditMode(binding.timeTextOne, binding.userCustomTitleOne);
            }
        });

        binding.editButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditMode(binding.timeTextTwo, binding.userCustomTitleTwo);
            }
        });
    }

    private void saveCustomTimes() {
        String timeText = workTime + "\n" + shortBreakTime + "\n" + longBreakTime;
        currentTimeText.setText(timeText);
        toggleEditMode();
    }

    private void onEditMode(TextView timeTextView, EditText titleEditText) {
        currentTimeText = timeTextView;
        currentTitleText = titleEditText;

        toggleEditMode();
    }

    private void toggleEditMode() {
        String textButton;

        if (isEditing) {
            isEditing = false;
            textButton = "Start";
        } else {
            isEditing = true;
            textButton = "Save";
        }
        currentTitleText.setFocusableInTouchMode(isEditing);
        currentTitleText.setCursorVisible(isEditing);
        currentTitleText.setFocusable(isEditing);
        binding.saveButton.setText(textButton);
    }

    private void updatePomodoro() {
        PomodoroTimer.updateTimerSettings(workTime, shortBreakTime, longBreakTime);
    }

    private void updateCustomTimes() {
        workTime = Integer.parseInt(binding.workText.getText().toString());
        shortBreakTime = Integer.parseInt(binding.shortBreakText.getText().toString());
        longBreakTime = Integer.parseInt(binding.longBreakText.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
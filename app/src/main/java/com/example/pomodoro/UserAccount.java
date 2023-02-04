package com.example.pomodoro;

import static com.example.pomodoro.LoginPage.databaseReference;

import com.google.firebase.database.DatabaseReference;

public class UserAccount {

    protected static String emailAddress;
    protected static String username;
    protected static String password;
    protected static String uid;

    protected static int pomodoroTotal;
    protected static int workTotal;
    protected static int breakTotal;
    protected static int pomodoroCycles = 0;

    protected static int customWorkOne = 25;
    protected static int customWorkTwo;
    protected static int customShortOne = 5;
    protected static int customShortTwo;
    protected static int customLongOne = 15;
    protected static int customLongTwo;


    public static String getEmailAddress() {
        return emailAddress;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setEmailAddress(String emailAddress) {
        UserAccount.emailAddress = emailAddress;
    }

    public static void setUsername(String username) {
        UserAccount.username = username;
    }

    public static void setPassword(String password) {
        UserAccount.password = password;
    }

    public static void setPomodoroTotal(int pomodoroTotal) {
        UserAccount.pomodoroTotal = pomodoroTotal;
    }

    public static void setWorkTotal(int workTotal) {
        UserAccount.workTotal = workTotal;
    }

    public static void setBreakTotal(int breakTotal) {
        UserAccount.breakTotal = breakTotal;
    }

    public static void setPomodoroCycles(int pomodoroCycles) {
        UserAccount.pomodoroCycles = pomodoroCycles;
    }

    public static void setUID(String uid) {
        UserAccount.uid = uid;
    }

    public static void increaseWorkTotal(long workTime) {
        workTotal+=workTime;

        String key = "Work Total";
        updateDatabase(key, workTotal);
    }

    public static void increaseBreakTotal(long breakTime) {
        breakTotal+=breakTime;

        String key = "Break Total";
        updateDatabase(key, breakTotal);
    }

    public static void incrementPomodoro() {
        pomodoroTotal++;

        String key = "Pomodoro Total";
        updateDatabase(key, pomodoroTotal);
    }

    public static void incrementCycles() {
        pomodoroCycles++;

        String key = "Pomodoro Cycle Total";
        updateDatabase(key, pomodoroCycles);
    }

    private static void updateDatabase(String key, int value) {
        DatabaseReference databaseStats = databaseReference.child("Users").child(uid).child("Statistics");
        databaseStats.child(key).setValue(value);
    }

    public static int getCustomWorkOne() {
        return customWorkOne;
    }

    public static void setCustomWorkOne(int customWorkOne) {
        UserAccount.customWorkOne = customWorkOne;
    }

    public static int getCustomWorkTwo() {
        return customWorkTwo;
    }

    public static void setCustomWorkTwo(int customWorkTwo) {
        UserAccount.customWorkTwo = customWorkTwo;
    }

    public static int getCustomShortOne() {
        return customShortOne;
    }

    public static void setCustomShortOne(int customShortOne) {
        UserAccount.customShortOne = customShortOne;
    }

    public static int getCustomShortTwo() {
        return customShortTwo;
    }

    public static void setCustomShortTwo(int customShortTwo) {
        UserAccount.customShortTwo = customShortTwo;
    }

    public static int getCustomLongOne() {
        return customLongOne;
    }

    public static void setCustomLongOne(int customLongOne) {
        UserAccount.customLongOne = customLongOne;
    }

    public static int getCustomLongTwo() {
        return customLongTwo;
    }

    public static void setCustomLongTwo(int customLongTwo) {
        UserAccount.customLongTwo = customLongTwo;
    }

    private UserAccount() {
        throw new UnsupportedOperationException();
    }
}

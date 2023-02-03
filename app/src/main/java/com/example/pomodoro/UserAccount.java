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

    public UserAccount() {
        throw new UnsupportedOperationException();
    }
}

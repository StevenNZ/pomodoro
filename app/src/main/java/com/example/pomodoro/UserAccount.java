package com.example.pomodoro;

import static com.example.pomodoro.LoginPage.databaseReference;

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

    public static void setUID(String uid) {
        UserAccount.uid = uid;
    }

    public static void increaseWorkTotal(long workTime) {
        workTotal+=workTime;
    }

    public static void increaseBreakTotal(long breakTime) {
        breakTotal+=breakTime;
    }

    public static void incrementPomodoro() {
        pomodoroTotal++;
    }

    public static void incrementCycles() {
        pomodoroCycles++;
    }

    public UserAccount() {
        throw new UnsupportedOperationException();
    }
}

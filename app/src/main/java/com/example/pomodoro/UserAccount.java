package com.example.pomodoro;

import static com.example.pomodoro.LoginPage.databaseReference;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;

public class UserAccount {

    protected static String emailAddress = "";
    protected static String username = "Guest";
    protected static String password;
    protected static String uid = "";
    protected static Uri uriImage = Uri.parse("android.resource://com.example.pomodoro/drawable/guest_icon");
    protected static int tomatoesCurrency = 0;

    protected static int pomodoroTotal;
    protected static int workTotal;
    protected static int breakTotal;
    protected static int pomodoroCycles = 0;

    protected static String customTitleOne = "Pomodoro";
    protected static String customTitleTwo = "Title";
    protected static int customWorkOne = 25;
    protected static int customWorkTwo;
    protected static int customShortOne = 5;
    protected static int customShortTwo;
    protected static int customLongOne = 15;
    protected static int customLongTwo;

    protected static boolean commonOne;
    protected static boolean commonTwo;
    protected static boolean commonThree;
    protected static boolean commonFour;
    protected static boolean rareOne;
    protected static boolean rareTwo;
    protected static boolean rareThree;
    protected static boolean rareFour;
    protected static boolean epicOne;
    protected static boolean epicTwo;
    protected static boolean epicThree;

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

    public static Uri getUriImage() {
        return uriImage;
    }

    public static void setUriImage(Uri uriImage) {
        UserAccount.uriImage = uriImage;
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

    public static int getPomodoroCycles() {
        return UserAccount.pomodoroCycles;
    }

    public static void setUID(String uid) {
        UserAccount.uid = uid;
    }

    public static void increaseWorkTotal(long workTime) {
        workTotal+=workTime;

        String key = "Work Total";
        updateDatabase("Statistics", key, workTotal);
    }

    public static void increaseBreakTotal(long breakTime) {
        breakTotal+=breakTime;

        String key = "Break Total";
        updateDatabase("Statistics", key, breakTotal);
    }

    public static void incrementPomodoro() {
        pomodoroTotal++;

        String key = "Pomodoro Total";
        updateDatabase("Statistics", key, pomodoroTotal);
    }

    public static void incrementCycles() {
        pomodoroCycles++;

        String key = "Pomodoro Cycle Total";
        updateDatabase("Statistics", key, pomodoroCycles);
    }

    public static int getTomatoes() {
        return tomatoesCurrency;
    }

    public static void setTomatoes(int tomatoes) {
        tomatoesCurrency = tomatoes;
    }

    public static void incrementTomatoes(int tomatoes) {
        tomatoesCurrency+=tomatoes;

        String key = "Tomatoes";
        updateDatabase("Inventory", key, tomatoesCurrency);
    }

    protected static void updateDatabase(String path, String key, Object value) {
        if (!uid.isEmpty()) {
            DatabaseReference databasePath = databaseReference.child("Users").child(uid).child(path);
            databasePath.child(key).setValue(value);
        }
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

    public static String getCustomTitleOne() {
        return customTitleOne;
    }

    public static void setCustomTitleOne(String customTitleOne) {
        UserAccount.customTitleOne = customTitleOne;
    }

    public static String getCustomTitleTwo() {
        return customTitleTwo;
    }

    public static void setCustomTitleTwo(String customTitleTwo) {
        UserAccount.customTitleTwo = customTitleTwo;
    }

    public static boolean isCommonOne() {
        return commonOne;
    }

    public static void setCommonOne(boolean commonOne) {
        UserAccount.commonOne = commonOne;
    }

    public static boolean isCommonTwo() {
        return commonTwo;
    }

    public static void setCommonTwo(boolean commonTwo) {
        UserAccount.commonTwo = commonTwo;
    }

    public static boolean isCommonThree() {
        return commonThree;
    }

    public static void setCommonThree(boolean commonThree) {
        UserAccount.commonThree = commonThree;
    }

    public static boolean isCommonFour() {
        return commonFour;
    }

    public static void setCommonFour(boolean commonFour) {
        UserAccount.commonFour = commonFour;
    }

    public static boolean isRareOne() {
        return rareOne;
    }

    public static void setRareOne(boolean rareOne) {
        UserAccount.rareOne = rareOne;
    }

    public static boolean isRareTwo() {
        return rareTwo;
    }

    public static void setRareTwo(boolean rareTwo) {
        UserAccount.rareTwo = rareTwo;
    }

    public static boolean isRareThree() {
        return rareThree;
    }

    public static void setRareThree(boolean rareThree) {
        UserAccount.rareThree = rareThree;
    }

    public static boolean isRareFour() {
        return rareFour;
    }

    public static void setRareFour(boolean rareFour) {
        UserAccount.rareFour = rareFour;
    }

    public static boolean isEpicOne() {
        return epicOne;
    }

    public static void setEpicOne(boolean epicOne) {
        UserAccount.epicOne = epicOne;
    }

    public static boolean isEpicTwo() {
        return epicTwo;
    }

    public static void setEpicTwo(boolean epicTwo) {
        UserAccount.epicTwo = epicTwo;
    }

    public static boolean isEpicThree() {
        return epicThree;
    }

    public static void setEpicThree(boolean epicThree) {
        UserAccount.epicThree = epicThree;
    }

    public static void resetGuest() {
        emailAddress = "";
        username = "Guest";
        uid = "";
        uriImage = Uri.parse("android.resource://com.example.pomodoro/drawable/guest_icon");
        tomatoesCurrency = 0;

        pomodoroTotal = 0;
        workTotal = 0;
        breakTotal = 0;
        pomodoroCycles = 0;

        customTitleOne = "Pomodoro";
        customTitleTwo = "Title";
        customWorkOne = 25;
        customWorkTwo = 0;
        customShortOne = 5;
        customShortTwo = 0;
        customLongOne = 15;
        customLongTwo = 0;

        commonOne = false;
        commonTwo = false;
        commonThree = false;
        commonFour = false;
        rareOne = false;
        rareTwo = false;
        rareThree = false;
        rareFour = false;
        epicOne = false;
        epicTwo = false;
        epicThree = false;
    }

    private UserAccount() {
        throw new UnsupportedOperationException();
    }


}

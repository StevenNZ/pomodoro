package com.example.pomodoro;

public class UserAccount {

    protected static String emailAddress;
    protected static String username;
    protected static String password;

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

    public UserAccount() {
        throw new UnsupportedOperationException();
    }
}

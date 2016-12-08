package com.hhj.mychat.event;

public class AddFriendEvent {
    public static final String TAG = "AddFriendEvent";

    public AddFriendEvent(String userName, String reason) {
        this.userName = userName;
        this.reason = reason;
    }

    public String userName;
    public String reason;
}

package me.codalot.core.setup;

public enum MessageType {

    CHAT,
    TITLE;

    public static MessageType from(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (Exception e) {
            return CHAT;
        }
    }

}

package com.slots.game.response;

public class Response<T> {
    private String message;
    private Status status;
    private T payload;


    public static enum Status {
        SUCCESS,
        DANGER,
        INFO;

        private Status() {
        }
    }

}


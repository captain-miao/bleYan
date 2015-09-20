package com.dahuo.learn.lbe.blelibrary;

public abstract class TimeoutCallback implements Runnable {
    public abstract void onTimeout();

    @Override
    public void run() {
        onTimeout();
    }
}
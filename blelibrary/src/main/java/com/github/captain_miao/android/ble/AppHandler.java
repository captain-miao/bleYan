package com.github.captain_miao.android.ble;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author YanLu
 * @since  2015-09-14
 */
public class AppHandler<T> extends Handler {
    private final WeakReference<T> mReference;
    protected HandleMessageListener<T> mMessageListener;

    public AppHandler(T reference) {
        this.mReference = new WeakReference<>(reference);
    }

    public AppHandler(T reference, HandleMessageListener<T> listener) {
        this.mReference = new WeakReference<>(reference);
        this.mMessageListener = listener;
    }

    @Override
    public final void handleMessage(Message msg) {
        T reference = mReference.get();
        if (reference != null && mMessageListener != null) {
            mMessageListener.onHandleMessage(reference, msg);
        }
    }

    public void setMessageListener(HandleMessageListener<T> messageListener) {
        this.mMessageListener = messageListener;
    }

    public HandleMessageListener<T> getMessageListener() {
        return mMessageListener;
    }

    public interface HandleMessageListener<T> {
        void onHandleMessage(T reference, Message msg);
    }

}

package com.huawei.bloomfate.util;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public abstract class SafeAsyncTask<T, Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private WeakReference<T> weakReference;

    public SafeAsyncTask(T reference) {
        weakReference = new WeakReference<>(reference);
    }

    protected boolean checkWeakReference() {
        return weakReference.get() == null;
    }

    protected T getReference() {
        return weakReference.get();
    }

}

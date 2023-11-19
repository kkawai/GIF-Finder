package com.kk.android.fuzzy_waddle.util;

import android.os.Handler;
import android.os.Looper;

import timber.log.Timber;

public abstract class AsyncTask<Params, Result> {

    private static final String TAG = "AsyncTask";

    protected abstract Result doInBackground(Params... params);

    protected void onPostExecute(Result result) {
    }

    @SafeVarargs
    public final void execute(final Params... params) {
        runOnWorkerThread(() -> {
            final Result result = doInBackground(params);
            runOnUiThread(() -> onPostExecute(result));
        });
    }

    private static void runOnUiThread(final Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            try {
                runnable.run();
            } catch (Throwable t) {
                Timber.tag(TAG).e(t, "runOnUiThread failed");
            }
            return;
        }
        new Handler(Looper.getMainLooper()).post(wrap(runnable));
    }

    static Runnable wrap(final Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                Timber.tag(TAG).e(t, "wrapped runnable failed:");
            }
        };
    }

    public static void runOnWorkerThread(final Runnable runnable) {
        new Thread(runnable).start();
    }
}

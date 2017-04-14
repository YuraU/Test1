package com.yura.test1;

import android.app.Application;;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private JobManager jobManager;
    private static MyApplication app;

    public static MyApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        configureJobManager();
    }

    private void configureJobManager() {

        Log.d(TAG, "configureJobManager START");
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {


                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                }).minConsumerCount(0)// always keep at least five consumer
                // alive
                .maxConsumerCount(10)// up to 10 consumers at a time
                .loadFactor(3)// 3 jobs per consumer
                .consumerKeepAlive(15)// wait 15 sec
                .build();
        jobManager = new JobManager(configuration);
        Log.d(TAG, "configureJobManager END");
    }

    public JobManager getJobManager() {

        return jobManager;
    }
}

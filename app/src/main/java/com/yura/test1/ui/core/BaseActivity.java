package com.yura.test1.ui.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.yura.test1.MyApplication;
import com.yura.test1.api.response.RequestResult;
import com.yura.test1.event.httpEvent.BaseHttpEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    protected JobManager jobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jobManager = MyApplication.getInstance().getJobManager();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseHttpEvent event) {
        if(event.getResult().getRequestResult().equals(RequestResult.SUCCESS)
            || event.getResult().getRequestResult().equals(RequestResult.CACHE)) {

            Map<String, Object> map = event.getResult().getTypedAnswer();

            onResponse(map, event.type);
        }else {
            onError(event.getResult().getRequestResult());
        }
    }

    protected void onResponse(Map<String, Object> data, int event){

    }

    protected void onError(RequestResult requestResult){
        String errorMsg;
        if(requestResult.equals(RequestResult.ERROR)){
            errorMsg = "Please check your network connection!";
        }else{
            errorMsg = "Server error!";
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                errorMsg, Toast.LENGTH_SHORT);
        toast.show();
    }

}

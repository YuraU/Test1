package com.yura.test1.ui;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yura.test1.R;
import com.yura.test1.adapter.DateAdapter;
import com.yura.test1.api.response.RequestResult;
import com.yura.test1.databinding.ActivityMainBinding;
import com.yura.test1.job.http.GetDateJob;
import com.yura.test1.ui.core.BaseActivity;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binder;
    private DateAdapter dateAdapter;
    private SaveFragment saveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        adapterSetup();

        saveFragment = (SaveFragment)getSupportFragmentManager().findFragmentByTag(SaveFragment.TAG);
        if(saveFragment == null){
            saveFragment = new SaveFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(saveFragment, SaveFragment.TAG)
                    .commit();
        }

        binder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.progressBar.setVisibility(View.VISIBLE);
                binder.button.setEnabled(false);
                jobManager.addJobInBackground(new GetDateJob());
            }
        });
    }


    private void adapterSetup() {
        if(dateAdapter == null) {
            dateAdapter = new DateAdapter(this);
        }

        binder.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binder.recyclerView.setAdapter(dateAdapter);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(saveFragment != null){
            saveFragment.setDates(dateAdapter.getDates());
        }

        outState.putInt("progressBarState", binder.progressBar.getVisibility());
        outState.putBoolean("buttonState", binder.button.isEnabled());

        if (dateAdapter != null) {
            dateAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (dateAdapter != null) {
            dateAdapter.setupItems(saveFragment.getDates());
            dateAdapter.restoreStates(savedInstanceState);
        }

        binder.progressBar.setVisibility(savedInstanceState.getInt("progressBarState", View.GONE));
        binder.button.setEnabled(savedInstanceState.getBoolean("buttonState", true));
    }

    private void setEnabled(){
        binder.progressBar.setVisibility(View.GONE);
        binder.button.setEnabled(true);
    }

    @Override
    protected void onResponse(Map<String, Object> data, int event) {
        setEnabled();
        String date = "" + data.get("date") + " " + data.get("time");

        dateAdapter.addItem(date);
    }

    @Override
    protected void onError(RequestResult requestResult) {
        super.onError(requestResult);
        setEnabled();
    }
}

package com.yura.test1.job.http;

import com.birbit.android.jobqueue.Params;
import com.yura.test1.api.response.Response;
import com.yura.test1.event.httpEvent.BaseHttpEvent;
import com.yura.test1.event.httpEvent.HttpEvent;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetDateJob extends BaseHttpJob {
    public static final int PRIORITY = 1;

    public GetDateJob() {
        super(new Params(PRIORITY));
    }

    @Override
    protected Call<ResponseBody> apiCall() {
        return service.getDate();
    }

    @Override
    protected HttpEvent getHttpEvent(Response response) {
        return new BaseHttpEvent(response, BaseHttpEvent.GET_DATA);
    }

    @Override
    protected void saveMemoryCache(Map<String, Object> map) {

    }

    @Override
    protected Map<String, Object> getMemoryCache() {
        return null;
    }
}

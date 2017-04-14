package com.yura.test1.job.http;

import com.birbit.android.jobqueue.Params;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yura.test1.api.ApiFactory;
import com.yura.test1.api.ApiService;
import com.yura.test1.api.response.RequestResult;
import com.yura.test1.api.response.Response;
import com.yura.test1.event.httpEvent.HttpEvent;
import com.yura.test1.job.BaseJob;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;


public abstract class BaseHttpJob extends BaseJob {

    public final static String TAG = "HttpJob";

    protected Response response;

    private final int id;
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    protected ApiService service = ApiFactory.getApiService();
    protected String jsonString;
    protected Map<String, Object> answer;
    protected String url;

    protected static final String GROUP = "http";

    protected BaseHttpJob(Params params) {
        super(params.groupBy(GROUP).addTags(TAG));
        id = jobCounter.incrementAndGet();
    }

    @Override
    public void onRun() throws Throwable {
        try {
            response = new Response();
            if(getMemoryCache() == null) {
                Call<ResponseBody> call = apiCall();
                getUrl(call);
                response.setAnswer(getHttpAnswer(call));
                checkResponseAnswer(response);
                if (response.getRequestResult() == RequestResult.SUCCESS) {
                    onSuccess();
                } else {
                    onError();
                }
            }else {
                response.setAnswer(getMemoryCache());
                response.setRequestResult(RequestResult.SUCCESS);
            }
        } catch (IOException e) {
            onError();
        }

        EventBus.getDefault().post(getHttpEvent(response));
    }

    protected void onSuccess() {
        saveResponse();
        saveMemoryCache(answer);
    }

    protected void onError(){
        Map<String, Object> cache = getHttpCache();

        if(cache == null) {
            response.setRequestResult(RequestResult.ERROR);
            return;
        }

        response.setAnswer(cache)
                .setRequestResult(RequestResult.CACHE);
    }

    protected void checkResponseAnswer(Response response){
        if(response.getAnswer() == null) {
            response.setRequestResult(RequestResult.ERROR);
        }else if(response.getAnswer().get("date") != null && response.getAnswer().get("time") != null){
            response.setRequestResult(RequestResult.SUCCESS);
        }else {
            response.setRequestResult(RequestResult.SERVER_ERROR);
        }
    }

    protected abstract Call<ResponseBody> apiCall();

    protected abstract HttpEvent getHttpEvent(Response response);

    protected void parseResponse(retrofit2.Response<ResponseBody> retrofitResponse) throws IOException {
        jsonString = retrofitResponse.body().string();
    }

    protected void getUrl(Call call){
        url = call.request().url().toString();
    }

    protected Map<String, Object> getHttpAnswer(Call<ResponseBody> call) throws IOException {
        retrofit2.Response<ResponseBody> retrofitResponse = call.execute();
        parseResponse(retrofitResponse);

        answer = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {}.getType());

        return answer;
    }

    protected void saveMemoryCache(Map<String, Object> map){

    }

    protected Map<String, Object> getMemoryCache(){
        return null;
    }

    protected void saveResponse(){

    }

    protected Map<String, Object> getHttpCache(){
        return null;
    }

}

package com.yura.test1.api.response;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Response {

    public class Answer extends HashMap<String, Object> {
        public Answer(Map<String, Object> answer) {
            super(answer);
        }

        @Override
        public Object get(Object key) {
            Object o = super.get(key);

            if(o == null)
                return "";

            return o;
        }
    }

    @Nullable
    private Answer mAnswer;

    private RequestResult mRequestResult;

    public Response() {
        mRequestResult = RequestResult.ERROR;
    }

    @NonNull
    public RequestResult getRequestResult() {
        return mRequestResult;
    }

    public Response setRequestResult(@NonNull RequestResult requestResult) {
        mRequestResult = requestResult;
        return this;
    }

    @Nullable
    public <T> T getTypedAnswer() {
        if (mAnswer == null) {
            return null;
        }
        return (T) mAnswer;
    }

    @NonNull
    public Response setAnswer(@Nullable Map<String, Object> answer) {
        if(answer == null){
            mAnswer = null;
            return this;
        }

        mAnswer = new Answer(answer);
        answer = null;
        return this;
    }

    @Nullable
    public Map getAnswer() {
        return mAnswer;
    }

    public void save(@NonNull Context context, String url, String jsonString) {
    }
}


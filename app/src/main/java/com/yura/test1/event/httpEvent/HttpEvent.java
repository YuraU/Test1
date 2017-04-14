package com.yura.test1.event.httpEvent;

import com.yura.test1.api.response.Response;

public abstract class HttpEvent {

    protected Response data;

    public HttpEvent(Response data) {
        this.data = data;
    }

    public Response getResult() {
        return data;
    }
}

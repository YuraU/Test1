package com.yura.test1.event.httpEvent;

import com.yura.test1.api.response.Response;

public class BaseHttpEvent extends HttpEvent {

    public final static int GET_DATA = 0;

    public int type;

    public BaseHttpEvent(Response data, int type) {
        super(data);

        this.type = type;
    }
}

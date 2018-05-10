package com.example.wristband.utils;



import com.example.wristband.bean.Doing;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.callback.Callback;


import java.io.IOException;
import java.util.List;

/**
 * Created by lql on 2018/4/23.
 */

public abstract class ListDoingCallback extends Callback<List<Doing>> {
    /**
     *
     * @param response
     * @return List<Doing>返回活动表
     * @throws IOException
     */
    @Override
    public List<Doing> parseNetworkResponse(Response response) throws IOException {
        String string = response.body().string();
        List<Doing> doings = new Gson().fromJson(string, List.class);
        return doings;
    }
}

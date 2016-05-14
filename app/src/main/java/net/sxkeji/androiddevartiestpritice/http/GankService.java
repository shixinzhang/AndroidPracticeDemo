package net.sxkeji.androiddevartiestpritice.http;

import net.sxkeji.androiddevartiestpritice.beans.GankBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 干货API接口
 * Created by zhangshixin on 5/13/2016.
 */
public interface GankService {
    @GET("all/{count}/{page}")
    Call<GankBean> getAllData(@Path("count") int count, @Path("page") int page);
}

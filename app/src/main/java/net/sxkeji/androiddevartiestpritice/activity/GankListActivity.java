package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.adapter.GankListAdapter;
import net.sxkeji.androiddevartiestpritice.beans.GankBean;
import net.sxkeji.androiddevartiestpritice.http.GankService;

import java.util.concurrent.Executor;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 干货列表
 * Created by zhangshixin on 5/13/2016.
 */
public class GankListActivity extends Activity {
    private final String TAG = "GankListActivity";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GankListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_list);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GankService gankService = retrofit.create(GankService.class);
        Call<GankBean> gankBeanCall = gankService.getAllData(20, 1);
        gankBeanCall.enqueue(new Callback<GankBean>() {
            @Override
            public void onResponse(Call<GankBean> call, Response<GankBean> response) {
                //It's in the main thread !
                GankBean gankBean = response.body();
                adapter = new GankListAdapter(GankListActivity.this, gankBean.getResults());
                recyclerView.setAdapter(adapter);
//                Log.e(TAG, "onResponse thread id : " + Thread.currentThread().getId());
                Log.e(TAG, "onResponse " + response.code() + " /msg " + response.message() + "/body " + response.body());
            }

            @Override
            public void onFailure(Call<GankBean> call, Throwable throwable) {
//                Log.e(TAG, "onFailure thread id : " + Thread.currentThread().getId());
                //It's in the main thread !
                Log.e(TAG, "onFailure " + throwable.getMessage());
            }
        });

//        gankBeanCall.cancel();
    }
}

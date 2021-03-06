package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.adapter.BaseQuickAdapter;
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
    private ProgressDialog dialog;
    private Call<GankBean> gankBeanCall;
    private GankBean upRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_list);
        ButterKnife.bind(this);
        initViews();
        loadData();
        setListeners();
    }

    private void setListeners() {
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载数据...");
        dialog.show();
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GankService gankService = retrofit.create(GankService.class);
        gankBeanCall = gankService.getAllData(20, 1);
        gankBeanCall.enqueue(new Callback<GankBean>() {
            @Override
            public void onResponse(Call<GankBean> call, Response<GankBean> response) {
                //It's in the main thread !
                final GankBean gankBean = response.body();
                setupRecyclerView(gankBean);
                Log.e(TAG, "onResponse " + response.code() + " /msg " + response.message() + "/body " + response.body());
            }

            @Override
            public void onFailure(Call<GankBean> call, Throwable throwable) {
                //It's in the main thread !
                Log.e(TAG, "onFailure " + throwable.getMessage());
                dismissDialog();
            }
        });

//        gankBeanCall.cancel();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        gankBeanCall.cancel();
    }

    /**
     * 填充数据
     */
    public void setupRecyclerView(final GankBean gankBean) {
        adapter = new GankListAdapter(GankListActivity.this, gankBean.getResults());
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = gankBean.getResults().get(position).getUrl();
                jump2BrowserWithUrl(url);
            }
        });
        recyclerView.setAdapter(adapter);
        dismissDialog();
    }

    /**
     * 浏览器打开指定url
     *
     * @param url
     */
    private void jump2BrowserWithUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        startActivity(intent);
    }
}

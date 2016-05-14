package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.adapter.LoadMoreRecyclerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用于聊天
 * RecyclerView上滑加载更多
 * Created by zhangshixin on 4/26/2016.
 */
public class RecyclerViewLoadDataActivity extends Activity {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private final String TAG = "RecyclerLoadMore";
    private LoadMoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> dataList; //所有数据
    private boolean isScrollToTop;      //判断是否向上滑动的标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_load_data);
        ButterKnife.bind(this);
        initData();
        initViews();
        setListeners();
    }

    private void setListeners() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滚动停止时判断是否滑到顶部
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //下面这2个值，第一个比第二个偏上一点，所以判断是否到顶部时使用第一个
                    int firstPos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//                    int firstPos2 = linearLayoutManager.findFirstVisibleItemPosition();

                    //下面这2个值，第二个比 第一个偏下一点, 所以判断是否到底部时使用第二个
//                    int lastPos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int lasPos2 = linearLayoutManager.findLastVisibleItemPosition();
//                    Log.e(TAG, "onScrollStateChanged first=" + firstPos + " / last="  + lasPos2);

                    if (isScrollToTop && firstPos == 0) {
                        Toast.makeText(RecyclerViewLoadDataActivity.this, "load more...", Toast.LENGTH_SHORT).show();
                        loadMoreData();
                    }
                }
            }

            /**
             * 滑动时调用， 根据dx、dy的值判断滑动方向
             * @param recyclerView
             * @param dx
             * @param dy
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled dx=" + dx + " / dy=" + dy);
                if (dy > 0) {
                    isScrollToTop = false;
                } else {
                    isScrollToTop = true;
                }
            }
        });
    }

    /**
     * 模拟请求、加载数据
     */
    private void loadMoreData() {
        ArrayList<String> moreData = new ArrayList<>();
        for (int i = 0 ; i < 15 ; i++){
            moreData.add("new data " + i);
        }
        moreData.addAll(dataList);
        dataList = moreData;
        adapter.setData(dataList);

    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dataList.add("item " + (i + 1));
        }
    }

    private void initViews() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new LoadMoreRecyclerAdapter(this);
        adapter.setData(dataList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        //设置显示底部元素
        linearLayoutManager.setStackFromEnd(true);
    }
}

package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;

import java.util.ArrayList;

/**
 * listview分页加载数据
 * Created by zhangshixin on 4/9/2016.
 */
public class ListViewLoadDataActivity extends Activity implements OnScrollListener {
    private ListView list_view;
    private ListAdapter adapter;
    private ArrayList<String> stringList;
    private View footerView;
    private Button btnLoadMore;
    private int visibleLastIndex;   //可见的最后一个item的位置
    private int visibleItemCount;   //页面可见的元素个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_load_data);
        initView();
    }

    private void initView() {
        list_view = (ListView) findViewById(R.id.list_view);
        initListFooterView();
        initListData();
        adapter = new ListAdapter(this, stringList);
        list_view.setAdapter(adapter);
        list_view.addFooterView(footerView);
        list_view.setOnScrollListener(this);
    }

    private void initListFooterView() {
        footerView = LayoutInflater.from(this).inflate(R.layout.item_list_footer, null);
        footerView.findViewById(R.id.btn_load_more);
        btnLoadMore = (Button) footerView.findViewById(R.id.btn_load_more);
    }

    private void initListData() {
        stringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringList.add("zsx" + i);
        }
    }

    /**
     * 滑动手势改变时调用
     *
     * @param view
     * @param scrollState SCROLL_STATE_IDLE = 0 "闲置" ;
     *                    SCROLL_STATE_TOUCH_SCROLL = 1 "滑动中" ;
     *                    SCROLL_STATE_FLING = 2 "滑动结束";
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("onScrollStateChanged", "scrollState " + scrollState);
        if (scrollState == SCROLL_STATE_IDLE && visibleLastIndex == adapter.getCount()) {
            btnLoadMore.setText("loading more.......");
            handler.sendEmptyMessageDelayed(1, 3000);
        }
    }

    /**
     * 滚动时调用
     *
     * @param view
     * @param firstVisibleItem 当前页面第一个item的索引
     * @param visibleItemCount 当前页面总共显示几个item
     * @param totalItemCount   该ListView总共几个item
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.e("onScroll ","firstVisibleItem:" + firstVisibleItem + "/visibleItemCount" + visibleItemCount +
//            " /totalItemCount " + totalItemCount);
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        Log.e("onScroll", "visibleLastIndex " + visibleLastIndex);
    }

    int j = 0;
    private void loadMoreData() {
        Log.e("loadMoreData","visibleLastIndex :" + visibleLastIndex + "/visibleItemCount: " + visibleItemCount);
        ArrayList<String> newDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newDataList.add("zsx new " + j++);
        }
        adapter.addData(newDataList);
        list_view.setSelection(visibleLastIndex - (visibleItemCount + 1)/2);    //设置显示新加载数据的一部分
        btnLoadMore.setText("load success");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    loadMoreData();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    class ListAdapter extends BaseAdapter {
        private ArrayList<String> strList;
        private Context context;


        public ListAdapter(Context context, ArrayList<String> strList) {
            this.context = context;
            this.strList = strList;
        }

        public void addData(ArrayList<String> newStrList) {
            strList.addAll(newStrList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return strList == null ? 0 : strList.size();
        }

        @Override
        public Object getItem(int position) {
            return strList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_detail, null);
            }
            TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            tvContent.setText(strList.get(position));

            return convertView;
        }
    }
}

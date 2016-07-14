package net.sxkeji.androiddevartiestpritice.activity.sidebarlist;


import android.content.Context;

import net.sxkeji.androiddevartiestpritice.adapter.BaseQuickAdapter;
import net.sxkeji.androiddevartiestpritice.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by zhangshixin on 7/11/2016.
 */
public class CarSeriesListAdapter extends BaseQuickAdapter<CarSeriesBean> {
    public CarSeriesListAdapter(Context context, List<CarSeriesBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    protected void convert(BaseViewHolder holder, CarSeriesBean item) {

    }
}

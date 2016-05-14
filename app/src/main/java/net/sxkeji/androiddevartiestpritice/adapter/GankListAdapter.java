package net.sxkeji.androiddevartiestpritice.adapter;

import android.content.Context;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.beans.GankBean;

import java.util.List;

/**
 * 干货list的adapter
 * Created by zhangshixin on 5/13/2016.
 */
public class GankListAdapter extends BaseQuickAdapter<GankBean.ResultsEntity> {
    public GankListAdapter(Context context, List<GankBean.ResultsEntity> data) {
        super(context, data, R.layout.item_recycler_gank);
    }

    @Override
    protected void convert(BaseViewHolder holder, GankBean.ResultsEntity item) {
        holder.setText(R.id.tv_title, item.getDesc())
                .setText(R.id.tv_type, item.getType())
                .linkify(R.id.tv_title);
    }
}

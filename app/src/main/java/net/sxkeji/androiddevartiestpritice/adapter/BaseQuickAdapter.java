package net.sxkeji.androiddevartiestpritice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 封装后的RecyclerAdapter
 * Created by zhangshixin on 5/13/2016.
 */
public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private List<T> mData;
    private int mLayoutId;

    public BaseQuickAdapter(Context context, List<T> data, int layoutId) {
        mContext = context;
        mData = data;
        mLayoutId = layoutId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new BaseViewHolder(mContext, mView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    protected abstract void convert(BaseViewHolder holder, T item);
}

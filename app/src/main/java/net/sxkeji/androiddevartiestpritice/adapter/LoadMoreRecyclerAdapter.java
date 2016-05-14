package net.sxkeji.androiddevartiestpritice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.beans.ProvinceBean;

import java.util.ArrayList;

/**
 * 上滑加载更多的RecyclerView Adapter
 * Created by zhangshixin on 4/26/2016.
 */
public class LoadMoreRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<String> mData;

    public LoadMoreRecyclerAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<String> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_load_more, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final String string = mData.get(position);
            ((ViewHolder) holder).tvName.setText(string);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }

        public TextView getTvName() {
            return tvName;
        }

        public void setTvName(TextView tvName) {
            this.tvName = tvName;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(ProvinceBean provinceBean, int position);
    }
}

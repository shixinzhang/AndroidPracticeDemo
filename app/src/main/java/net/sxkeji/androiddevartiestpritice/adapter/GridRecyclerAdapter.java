package net.sxkeji.androiddevartiestpritice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.beans.CityBean;

import java.util.ArrayList;

/**
 * 选择城市网格列表
 * Created by zhangshixin on 4/11/2016.
 */
public class GridRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<CityBean> mData;
    private OnItemClickListener mListener;

    public GridRecyclerAdapter(Context context){
        mContext = context;
    }

    public void setData(ArrayList<CityBean> data){
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_city_select,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            final CityBean cityBean = mData.get(position);

            Log.e("GridRecyclerAdapter", cityBean.getName() + "");
            ((ViewHolder) holder).tvContent.setText(cityBean.getName());
            if (mListener != null){
                ((ViewHolder) holder).tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.OnItemClick(cityBean);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(CityBean cityBean);
    }
}

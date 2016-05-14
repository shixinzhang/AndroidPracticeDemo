package net.sxkeji.androiddevartiestpritice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.beans.CityBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 城市的RecyclerView Adapter
 * Created by zhangshixin on 4/11/2016.
 */
public class CityRecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<CityBean> mData;
    private OnItemClickListener mListener;
    private HashMap<Integer, Boolean> mSelectPos;

    public CityRecyclerAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<CityBean> data) {
        mData = data;
        mSelectPos = new HashMap<>();
        for (int i = 0; i < getItemCount(); i++) {
            mSelectPos.put(i, false);
        }
        notifyDataSetChanged();
    }

    public void setData(ArrayList<CityBean> data, HashMap<Integer, Boolean> selectPos) {
        mData = data;
        mSelectPos = selectPos;
        notifyDataSetChanged();
    }


    public void setSelectPos(int position) {
        if (mSelectPos != null && mSelectPos.size() >= position) {
            Boolean isSelect = mSelectPos.get(position);
            if (isSelect == null || isSelect) {
                mSelectPos.put(position, false);
            } else {
                mSelectPos.put(position, true);
            }
            notifyDataSetChanged();
        }
    }
    public boolean getSelectPos(int position){
        if (position <= getItemCount()) {
            return mSelectPos.get(position);
        }else {
            return false;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_selec_city, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final CityBean cityBean = mData.get(position);
            Boolean isSelect = mSelectPos.get(position);
            ((ViewHolder) holder).tvContent.setText(cityBean.getName());
            if (isSelect != null && isSelect) {
                ((ViewHolder) holder).ivSelect.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolder) holder).ivSelect.setVisibility(View.INVISIBLE);
            }
            if (mListener != null) {
                ((ViewHolder) holder).tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.OnItemClick(cityBean, position);
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private ImageView ivSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_name);
            ivSelect = (ImageView) itemView.findViewById(R.id.iv_select);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(CityBean cityBean, int position);
    }
}

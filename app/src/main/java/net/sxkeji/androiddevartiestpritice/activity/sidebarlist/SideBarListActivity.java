package net.sxkeji.androiddevartiestpritice.activity.sidebarlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.activity.sidebarlist.sortlistview.SortFramlayout;
import net.sxkeji.androiddevartiestpritice.activity.sidebarlist.sortlistview.SortModel;
import net.sxkeji.androiddevartiestpritice.adapter.quickadapter.QuickAdapter;
import net.sxkeji.androiddevartiestpritice.widget.GsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 侧边栏和抽屉组合
 * Created by zhangshixin on 7/11/2016.
 */
public class SideBarListActivity extends Activity {
    @Bind(R.id.sortframlayout)
    SortFramlayout sortframlayout;
    @Bind(R.id.drawer_car_series)
    RelativeLayout drawerCarSeries;
    @Bind(R.id.drawlayout)
    DrawerLayout drawlayout;
    @Bind(R.id.iv_logo)
    ImageView ivSelectBrandLogo;
    @Bind(R.id.tv_name)
    TextView tvSelectBrandName;
    @Bind(R.id.recycler_car_series)
    RecyclerView recyclerCarSeries;
    private List<SortModel> brandList;
    private QuickAdapter<SortModel> hotBrandBeanQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_car);
        ButterKnife.bind(this);

        initLocalData();
        initViews();
        setListener();
    }

    private void initViews() {
        if (brandList != null) {
            sortframlayout.setData(null, brandList);
            sortframlayout.setRefresh(false);
        } else {
            Log.e("汽车品牌列表", "汽车列表为空");
        }
    }

    private void setListener() {
        sortframlayout.setOnItemClickListener(new SortFramlayout.SortListviewOnitemClickInterface() {
            @Override
            public void onItemClick(List<SortModel> sortModels, int position) {
                SortModel sortModel = sortModels.get(position);
                if (sortModel != null && !TextUtils.isEmpty(sortModel.getName())) {
                    tvSelectBrandName.setText(sortModel.getName());
                    String logoUrl = sortModel.getLogoUrl();
                    if (!TextUtils.isEmpty(logoUrl)) {
                        Picasso.with(SideBarListActivity.this).load(logoUrl)
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .into(ivSelectBrandLogo);
                    }
                }
                setDrawlayout();
            }
        });
    }

    /**
     * 本地数据
     */
    private void initLocalData() {
        try {
            InputStream inputStream = getAssets().open("allbrand.json.txt");
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            String localBrandJson = new String(bytes);

            //加载成功，解析
            if (!TextUtils.isEmpty(localBrandJson)) {
                CarBrandBean carBrandBean = (CarBrandBean) GsonUtils.jsonToBean(localBrandJson, CarBrandBean.class);
                brandList = carBrandBean.getCarBrandList();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("汽车品牌列表", "汽车列表加载失败" + e.getMessage());
        }
    }

    /**
     * 抽屉的展开与关闭
     **/
    private void setDrawlayout() {
        if (drawlayout.isDrawerOpen(drawerCarSeries)) {
            drawlayout.closeDrawer(GravityCompat.END);
        } else {
            drawlayout.openDrawer(GravityCompat.END);
        }
        drawlayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭滑动
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawlayout != null && drawlayout.isDrawerOpen(drawerCarSeries)) {
            drawlayout.closeDrawers();
            return;
        }
        finish();
    }
}

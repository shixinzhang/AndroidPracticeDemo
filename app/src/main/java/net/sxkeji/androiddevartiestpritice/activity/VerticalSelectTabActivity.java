package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.beans.CityBean;
import net.sxkeji.androiddevartiestpritice.adapter.CityRecyclerAdapter;
import net.sxkeji.androiddevartiestpritice.adapter.GridRecyclerAdapter;
import net.sxkeji.androiddevartiestpritice.beans.ProvinceBean;
import net.sxkeji.androiddevartiestpritice.adapter.ProvinceRecyclerAdapter;
import net.sxkeji.androiddevartiestpritice.adapter.SelectProvinceCityBean;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 竖直tab选择器
 * Created by zhangshixin on 4/11/2016.
 */
public class VerticalSelectTabActivity extends Activity {

    @Bind(R.id.recycler_grid)
    RecyclerView recyclerGrid;
    @Bind(R.id.img_sellable_zone)
    ImageView imgSellableZone;
    @Bind(R.id.rl_sellable_zone)
    RelativeLayout rlSellableZone;
    @Bind(R.id.img_disable_zone)
    ImageView imgDisableZone;
    @Bind(R.id.rl_disable_zone)
    RelativeLayout rlDisableZone;
    @Bind(R.id.recycler_province)
    RecyclerView recyclerProvince;
    @Bind(R.id.recycler_city)
    RecyclerView recyclerCity;
    @Bind(R.id.ll_select_province)
    LinearLayout llSelectProvince;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager provinceLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private GridRecyclerAdapter selectGridAdapter;
    private ArrayList<CityBean> selectCityBeanList;     //选择的城市列表
    private ArrayList<ProvinceBean> allProvinceList;
    private ArrayList<TextView> selectProvinceTextViewList; //保存添加到"当前选择"的省份，为了准备以后的点击事件
    private ProvinceRecyclerAdapter provinceAdapter;
    private CityRecyclerAdapter cityAdapter;
    private HashMap<String, Integer> provinceIndexMap;              //   根据省份名称找到它在 "当前选择"里的位置
    private HashMap<Integer, Boolean> citySelectMap;               //  选择的城市在省份里对应的位置与是否显示图标
    private ArrayList<HashMap<Integer, Boolean>> citySelectedList;   //  选择城市列表，索引为上面provinceIndexMap中省份对应的index
    private int provinceIndex = -1;
    private ArrayList<SelectProvinceCityBean> selectBeanList;   //***最终要保留的重要数据***  选择省份、城市的实体类list
    private SelectProvinceCityBean selectProvinceCityBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_select);
        ButterKnife.bind(this);
        initViews();
        setListeners();
    }

    private void setListeners() {
//                selectGridAdapter.setData(getGridData2());
        rlSellableZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisableZone.setVisibility(View.INVISIBLE);
                imgSellableZone.setVisibility(View.VISIBLE);
            }
        });

        rlDisableZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDisableZone.setVisibility(View.VISIBLE);
                imgSellableZone.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initViews() {
        initProvinceData();
        initGridRecyclerView();
        provinceIndexMap = new HashMap<>();
        citySelectedList = new ArrayList<>();
        selectProvinceTextViewList = new ArrayList<>();
        selectBeanList = new ArrayList<>();

        cityAdapter = new CityRecyclerAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        cityAdapter.setData(allProvinceList.get(0).getCitys());
        cityAdapter.setOnItemClickListener(new CityRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(CityBean cityBean, int position) {
                cityAdapter.setSelectPos(position);
                Boolean isSelect = citySelectMap.get(position);
                if (isSelect == null || isSelect) {
                    citySelectMap.put(position, false);
                    addSelectProvinceAndCity(cityBean, false);
                    Log.e("cityAdapter click", "citySelectMap put " + position + " false");
                } else if (!isSelect) {
                    citySelectMap.put(position, true);
                    addSelectProvinceAndCity(cityBean, true);
                    Log.e("cityAdapter click", "citySelectMap put " + position + " true");
                }
                Log.e("CityOnItemClick", " /provinceIndex " + provinceIndex);
                if (citySelectedList.size() <= provinceIndex) {
                    citySelectedList.add(citySelectMap);
                } else {
                    citySelectedList.set(provinceIndex, citySelectMap);
                }
            }
        });
        recyclerCity.setLayoutManager(linearLayoutManager);
        recyclerCity.setAdapter(cityAdapter);

        provinceLayoutManager = new LinearLayoutManager(this);
        provinceAdapter = new ProvinceRecyclerAdapter(this, 0);
        provinceAdapter.setData(allProvinceList);
        provinceAdapter.setOnItemClickListener(new ProvinceRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ProvinceBean provinceBean, int position) {
                ArrayList<CityBean> cityList = provinceBean.getCitys();
                SelectProvinceCityBean temp = new SelectProvinceCityBean();     //临时将ProvinceBean 包装成SelectProvinceCityBean，为了下一步 比较
                temp.setSelectProvince(provinceBean);

                if (!selectBeanList.contains(temp)) {   //之前没选择过这个省份
//                    Log.e("OnItemClick",provinceBean.getName() + " /provinceIndex " + provinceIndex);
                    citySelectMap = new HashMap<Integer, Boolean>();
                    for (int i = 0; i < provinceBean.getCitys().size(); i++) {
                        citySelectMap.put(i, false);
                    }
                    selectProvinceCityBean = temp;
                    selectCityBeanList = new ArrayList<>();
                    selectProvinceCityBean.setSelectCityList(selectCityBeanList);
                    //显示城市数据
                    if (cityList != null) {
                        cityAdapter.setData(cityList);
                    }
                } else {                //之前选择过这个省份
                    Integer provinceId = provinceIndexMap.get(provinceBean.getName());  //根据名称获取选择省份的id
                    Log.e("OnItemClick", " provinceId " + provinceId + " / selectBeanList.size " + selectBeanList.size());
                    selectProvinceCityBean = selectBeanList.get(provinceId);
                    if (citySelectedList != null && citySelectedList.size() > 0) {
                        citySelectMap = citySelectedList.get(provinceId);
                        Log.e("provinceAdapter","citySelectMap " + citySelectMap.toString());
                        //显示城市个选择图片状态
                        if (cityList != null && citySelectMap != null) {
                            cityAdapter.setData(cityList, citySelectMap);
                        }
                    } else {
                        cityAdapter.setData(cityList);
                    }
                }

                provinceAdapter.changeSelecPos(position);       //改变选择背景状态

            }
        });
        recyclerProvince.setLayoutManager(provinceLayoutManager);
        recyclerProvince.setAdapter(provinceAdapter);


    }

    /**
     * 选择了城市，添加到顶部GridView
     *
     * @param cityBean
     * @param isAdd    true为添加，false为删除
     */
    private void addSelectProvinceAndCity(CityBean cityBean, boolean isAdd) {
        //选择的城市不重复，添加到对应的省份下
        ArrayList<CityBean> selectCityList = selectProvinceCityBean.getSelectCityList();
        if (!selectCityList.contains(cityBean)) {
            Log.e("addSelectCity", cityBean.getName() + "");
            recyclerGrid.setVisibility(View.VISIBLE);
            Log.e("addSelectCity", cityBean.getName() + "isAdd");
            selectCityList.add(cityBean);

        } else {
            Log.e("addSelectCity", cityBean.getName() + "isDelete");
            selectCityList.remove(cityBean);
//            if (selectCityList.size() == 0){
//                recyclerGrid.setVisibility(View.INVISIBLE);
//            }
        }
        selectGridAdapter.setData(selectCityList);
        selectProvinceCityBean.setSelectCityList(selectCityList);

        //选择的省份不重复时 添加到 选择set中
        if (!selectBeanList.contains(selectProvinceCityBean)) {
            String provinceName = selectProvinceCityBean.getSelectProvince().getName();
            provinceIndexMap.put(provinceName, ++provinceIndex);      //初始化这个省份对应的index
            final ProvinceBean selectProvince = selectProvinceCityBean.getSelectProvince();
            TextView tvSelectProvince = new TextView(VerticalSelectTabActivity.this);
            tvSelectProvince.setText(selectProvince.getName());
            tvSelectProvince.setTextColor(Color.parseColor("#111111"));
            tvSelectProvince.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(22, 8, 16, 8);
            tvSelectProvince.setLayoutParams(params);
            //显示添加的省份
            llSelectProvince.addView(tvSelectProvince);
            selectProvinceTextViewList.add(tvSelectProvince);

//            Toast.makeText(VerticalSelectTabActivity.this,"provinceIndex" + provinceIndex,Toast.LENGTH_SHORT).show();
            selectBeanList.add(selectProvinceCityBean);
            setSelectProvinceTextOnClickListeners();
        }

    }

    /**
     * 设置顶部选择省份的点击事件 <br />
     * 切换选择城市列表
     */
    private void setSelectProvinceTextOnClickListeners() {
        for (int i = 0; i < selectProvinceTextViewList.size(); i++) {
            TextView tvProvince = selectProvinceTextViewList.get(i);
            final int finalI = i;
            tvProvince.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGridAdapter.setData(selectBeanList.get(finalI).getSelectCityList());
                }
            });
        }
    }

    private void initProvinceData() {
        allProvinceList = new ArrayList<>();
        for (int i = 1; i < 14; i++) {
            ProvinceBean provinceBean = new ProvinceBean();
            if (i == 1) {
                provinceBean.setName("全国");
            } else {
                provinceBean.setName("陕西省" + i);
            }
            ArrayList<CityBean> cityBeans = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                CityBean cityBean = new CityBean();
                if (j == 0) {
                    cityBean.setName("全部");
                } else {
                    cityBean.setName("西安市" + j);
                }
                cityBeans.add(cityBean);
            }
            provinceBean.setCitys(cityBeans);
            allProvinceList.add(provinceBean);
        }
    }

    private void initGridRecyclerView() {
//        setGridData();
        gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerGrid.setLayoutManager(gridLayoutManager);
        selectGridAdapter = new GridRecyclerAdapter(this);

//        selectGridAdapter.setData(selectCityList);
        recyclerGrid.setAdapter(selectGridAdapter);

    }

    private ArrayList<CityBean> getGridData3() {
        ArrayList<CityBean> selectList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            CityBean cityBean = new CityBean();
            cityBean.setName("西班牙" + i);
            selectList.add(cityBean);
        }
        return selectList;
    }

    private ArrayList<CityBean> getGridData2() {
        ArrayList<CityBean> selectList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            CityBean cityBean = new CityBean();
            cityBean.setName("北京" + i);
            selectList.add(cityBean);
        }
        return selectList;
    }


}

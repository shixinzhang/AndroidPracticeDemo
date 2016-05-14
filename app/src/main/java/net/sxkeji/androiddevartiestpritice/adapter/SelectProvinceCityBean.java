package net.sxkeji.androiddevartiestpritice.adapter;

import net.sxkeji.androiddevartiestpritice.beans.CityBean;
import net.sxkeji.androiddevartiestpritice.beans.ProvinceBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 选择的省份、城市实体类
 * Created by zhangshixin on 4/12/2016.
 */
public class SelectProvinceCityBean implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private ProvinceBean selectProvince;
    private ArrayList<CityBean> selectCityList;

    public ProvinceBean getSelectProvince() {
        return selectProvince;
    }

    public void setSelectProvince(ProvinceBean selectProvince) {
        this.selectProvince = selectProvince;
    }

    public ArrayList<CityBean> getSelectCityList() {
        return selectCityList;
    }

    public void setSelectCityList(ArrayList<CityBean> selectCityList) {
        this.selectCityList = selectCityList;
    }

    /**
     * 重写equals方法，便于后续ArrayList<SelectProvinceCityBean>中判断是否重复
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        SelectProvinceCityBean provinceCityBean = (SelectProvinceCityBean) o;
        String name = provinceCityBean.getSelectProvince().getName();
        return getSelectProvince().getName().equals(name);
    }
}

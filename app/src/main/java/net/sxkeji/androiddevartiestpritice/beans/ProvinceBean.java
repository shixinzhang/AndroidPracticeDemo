package net.sxkeji.androiddevartiestpritice.beans;

import net.sxkeji.androiddevartiestpritice.beans.CityBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangshixin on 4/11/2016.
 */
public class ProvinceBean implements Serializable{
    private String Name;
    private long Id;
    private ArrayList<CityBean> Citys;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public ArrayList<CityBean> getCitys() {
        return Citys;
    }

    public void setCitys(ArrayList<CityBean> citys) {
        Citys = citys;
    }
}

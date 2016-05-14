package net.sxkeji.androiddevartiestpritice.beans;

import java.io.Serializable;

/**
 * 城市实体类
 * Created by zhangshixin on 4/11/2016.
 */
public class CityBean implements Serializable {
    private String Name;
    private long Id;
    private long ProvinceId;
    private boolean isSelected; //是否选中

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

    public long getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(long provinceId) {
        ProvinceId = provinceId;
    }

    /**
     * 重写equals方法，便于后续ArrayList<CityBean>中判断是否重复
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        CityBean cityBean = (CityBean) o;   //强转为CityBean
        return getName().equals(cityBean.getName());
    }
}

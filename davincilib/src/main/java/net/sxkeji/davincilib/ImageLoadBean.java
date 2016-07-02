package net.sxkeji.davincilib;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * 图片加载实体类，
 * 记录要加载的图片的 URL、Bitmap 以及 ImageView
 * Created by zhangshixin on 6/8/2016.
 */
public class ImageLoadBean implements Serializable {
    private String url;
    private Bitmap bitmap;
    private ImageView imageView;

    private ImageLoadBean() {
    }

    public ImageLoadBean(String url, Bitmap bitmap, ImageView imageView) {
        this.url = url;
        this.bitmap = bitmap;
        this.imageView = imageView;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}

package net.sxkeji.davincilib;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * 图片缩放处理类
 * Created by zhangshixin on 5/23/2016.
 */
public class ImageResizer {
    private final String TAG = "ImageResizer";

    /**
     * 压缩加载本地资源图片
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //第一次设置inJustDecodeBounds = true，表示只测量尺寸并不加载
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //计算缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //设置inJustDecodeBounds = false, 表示加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 压缩加载文件描述符，文件描述符其实就是一个中介，通过它可以拿到该文件的输入输出流
     *
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //第一次设置inJustDecodeBounds = true，表示只测量尺寸并不加载
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        //计算缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //设置inJustDecodeBounds = false, 表示加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 计算要缩放的尺寸比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqHeight == 0 || reqWidth == 0) {
            return 1;
        }
        //图片实际的尺寸
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;
        Log.d(TAG, "图片实际尺寸: width = " + outWidth + "/height = " + outHeight);
        int inSampleSize = 1;

        if (outHeight > reqHeight || outWidth > reqWidth) {
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            //如果缩放后仍比要求的大，继续缩放，inSampleSize是2的幂次增长
            while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "缩放比例: inSampleSize = " + inSampleSize);

        return inSampleSize;
    }
}

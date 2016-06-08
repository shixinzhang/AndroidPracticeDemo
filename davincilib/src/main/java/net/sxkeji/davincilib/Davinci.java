package net.sxkeji.davincilib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

/**
 * Davinci is friend of Picasso,
 * so he can do the same thing as Picasso --- load image !
 * <p>
 * Three levels for loading image :
 * 1.memory
 * 2.disk
 * 3.network
 * <p>
 * <p>
 * Created by zhangshixin on 6/8/2016.
 */
public class Davinci {
    private static final String TAG = "DavinciImageLoader";
    private Context context;
    private DiskLruCacheUtils diskCache;
    private LruCacheUtils memoryCache;

    public Davinci(Context context) throws IOException {
        this.context = context.getApplicationContext();
        this.diskCache = DiskLruCacheUtils.getInstance(context);
        this.memoryCache = new LruCacheUtils();
    }

    /**
     * 同步加载图片按顺序分三步来:
     * <p>
     * 1.memory
     * 2.disk
     * 3.network
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        //先从内存中加载
        Bitmap bitmap = loadBitmapFromMemoryCache(url);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemoryCache, url is: " + url);
            return bitmap;
        }

        //其次硬盘中
        bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromDiskCache, url is: " + url);
            return bitmap;
        }

        //都没有就重下载吧
        bitmap = loadBitmapFromNetWork(url, reqWidth, reqHeight);
        Log.d(TAG, "loadBitmapFromNetWork, url is: " + url);
        return bitmap;
    }

    /**
     * 添加内存缓存
     *
     * @param key
     * @param bitmap
     */
    private void addBitmap2MemoryCache(String key, Bitmap bitmap) {
        if (loadBitmapFromMemoryCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存中加载图片
     *
     * @param key
     * @return
     */
    private Bitmap loadBitmapFromMemoryCache(String key) {
        return memoryCache.get(key);
    }

    /**
     * 从硬盘中加载图片
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "Load bitmap from UI thread, it may causes ANR !");
        }
        if (diskCache == null) {
            return null;
        }
        return diskCache.getBitmapFromDisk(url, reqWidth, reqHeight);
    }

    /**
     * 从网络加载图片，顺便缓存到硬盘
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromNetWork(String url, int reqWidth, int reqHeight) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI thread !");
        }
        if (diskCache == null) {
            return null;
        }

        return diskCache.saveBitmap2Disk(url, reqWidth, reqHeight);
    }
}

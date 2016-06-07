package net.sxkeji.davincilib;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;

/**
 * 内存缓存
 * LruCache: LinkedHashMap 、 强引用
 * Created by zhangshixin on 6/7/2016.
 */
public class LruCacheUtils {
    //最大内存，单位为KB
    private static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    //缓存占用内存的八分之一
    private static int cacheSize = maxMemory / 8;

    private static LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize) {
        /**
         * 计算缓存的大小
         * @param key
         * @param bitmap
         * @return 单位也是KB
         */
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            else {
                return bitmap.getAllocationByteCount() * bitmap.getHeight() / 1024;
            }
        }

        /**
         * 移除旧缓存时调用，可以在这里完成一些资源回收操作
         * @param evicted
         * @param key
         * @param oldValue
         * @param newValue
         */
        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
        }
    };

    /**
     * 获取指定key的bitmap
     * @param key
     * @return
     */
    public static Bitmap get(String key){
        if (TextUtils.isEmpty(key)){
            return null;
        }

        return memoryCache.get(key);
    }

    /**
     * 添加元素到缓存
     * @return
     */
    public static void put(String key,Bitmap value){
        if (TextUtils.isEmpty(key)){
            return ;
        }

        memoryCache.put(key,value);
    }
}

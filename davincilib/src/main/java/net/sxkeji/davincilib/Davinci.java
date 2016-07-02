package net.sxkeji.davincilib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Davinci is friend of Picasso,
 * so he can do the same thing as Picasso --- load image !
 * <p/>
 * Three levels for loading image :
 * 1.memory
 * 2.disk
 * 3.network
 * <p/>
 * <p/>
 * Created by zhangshixin on 6/8/2016.
 */
public class Davinci {
    private static final String TAG = "DavinciImageLoader";
    private static final int IMG_TAG = 11;
    private Context context;
    private DiskLruCacheUtils diskCache;
    private LruCacheUtils memoryCache;
    private WeakReference weakReference;

    public Davinci(Context context) throws IOException {
        this.context = context.getApplicationContext();
        this.diskCache = DiskLruCacheUtils.getInstance(context);
        this.memoryCache = new LruCacheUtils();
    }

    /**
     * 直接用主线程的Looper，这样在非主线程也可以调用 Davinci加载图片
     */
    private static Handler mainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageLoadBean loadBean = (ImageLoadBean) msg.obj;
            if (loadBean != null){
                ImageView imageView = loadBean.getImageView();
                Bitmap bitmap = loadBean.getBitmap();
                String url = loadBean.getUrl();
                String tagUrl = (String) imageView.getTag(IMG_TAG);

                if (!TextUtils.isEmpty(tagUrl) && tagUrl.equals(url)){          //标记要加载到的位置，防止错位
                    imageView.setImageBitmap(bitmap);
                }else {
                    Log.d(TAG,"Ignore set bitmap, position or url changed ! ");
                }
            }
        }
    };

    /**
     * 同步加载图片
     * <p/>
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
     * 异步加载图片
     *
     * @return
     */
    public void loadBitmapAsync(final String url, final ImageView imageView, final int reqWidth, final int reqHeight) {
        //设置标签
        imageView.setTag(IMG_TAG, url);
        Bitmap bitmap = loadBitmapFromMemoryCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        //线程中进行加载
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap loadBitmap = loadBitmap(url, reqWidth, reqHeight);
                if (loadBitmap != null) {
                    ImageLoadBean loadBean = new ImageLoadBean(url, loadBitmap, imageView);

                }
            }
        };
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

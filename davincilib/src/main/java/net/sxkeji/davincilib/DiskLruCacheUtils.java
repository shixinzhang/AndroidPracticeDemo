package net.sxkeji.davincilib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import net.sxkeji.davincilib.lib.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 硬盘缓存
 *
 * How to use?
 * 1.invoke saveBitmap2Disk(ImageURL) to save bitmap to disk
 * 2.then, save the URLs to SharedPreferences or file
 * 3.next time your app start, invoke getBitmapFromDisk(URL) to load bitmap
 * 4.if the return of getBitmapFromDisk is null, it's means that your disk cache had been clear,
 *      you need to recycle to the first step ↑
 *
 * Created by zhangshixin on 6/7/2016.
 */
public class DiskLruCacheUtils {
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50; //50M
    private static final int DISK_CACHE_INDEX = 0;  //默认DiskLruCache的一个节点只有一个数据，所以索引为0
    private static final int IO_BUFFER_SIZE = 8 * 1024;     //IO缓存流大小

    private static DiskLruCache diskLruCache;
    private static DiskLruCacheUtils instance;
    private static ImageResizer imageResizer;   //压缩图片

    private DiskLruCacheUtils() {
    }

    public static DiskLruCacheUtils getInstance(Context context) throws IOException {
        if (instance == null) {
            instance = new DiskLruCacheUtils();
            setImageResizer(new ImageResizer());

            File diskCacheDir = getDiskCacheDir(context, "sxkeji");
            if (!diskCacheDir.exists()) {
                diskCacheDir.mkdirs();
            }
            setDiskLruCache(DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE));
        }
        return instance;
    }

    /**
     * 保存到硬盘
     *
     * @param url
     * @return
     */
    public Bitmap saveBitmap2Disk(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String key = hashKeyFromUrl(url);
        try {
            DiskLruCache.Editor editor = getDiskLruCache().edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (downloadUrl2Stream(url, outputStream)) {
                    //提交写入操作，释放锁
                    editor.commit();
                } else {
                    //下载异常，结束操作
                    editor.abort();
                }

                outputStream.close();
                getDiskLruCache().flush();

                return getBitmapFromDisk(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从硬盘缓存中读取位图
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromDisk(String url) {
        if (TextUtils.isEmpty(url) || getDiskLruCache() == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        try {
            //由key获取Snapshot
            DiskLruCache.Snapshot snapshot = getDiskLruCache().get(key);
            if (snapshot != null) {
                //由Snapshot获取输入流
                FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(IO_BUFFER_SIZE);
                //压缩图片会影响FileInputStream的序列，通过文件描述符来操作不会影响
                FileDescriptor fileDescriptor = fileInputStream.getFD();
//                getImageResizer().decodeBitmapFromFileDescriptor(fileDescriptor, )

                // TODO: 6/8/2016 目前先不压缩，因为压缩比例不确定
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 下载指定URL内容到文件输出流
     *
     * @param urlString
     * @param ops
     * @return
     */
    private boolean downloadUrl2Stream(String urlString, OutputStream ops) throws IOException {
        //如果当前线程是在主线程 则报异常
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (TextUtils.isEmpty(urlString) || ops == null) {
            return false;
        }
        HttpURLConnection urlConnection = null;
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        URL url = new URL(urlString);

        //try-with-resource needs API 19, forget it
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            outputStream = new BufferedOutputStream(ops, IO_BUFFER_SIZE);
            int b;
            while ((b = inputStream.read()) != -1) {
                outputStream.write(b);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return false;
    }

    /**
     * 将图片URL进行MD5加密后作为key存储
     *
     * @param url
     * @return
     */
    private String hashKeyFromUrl(String url) {
        String hashKey;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            hashKey = bytes2HexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //直接用hash值
            hashKey = String.valueOf(url.hashCode());
        }
        return hashKey;
    }

    /**
     * 将二进制字节流组装为一个String
     *
     * @param digest
     * @return
     */
    private String bytes2HexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //与一个11变成十进制
            String hex = Integer.toHexString(digest[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取文件目录
     *
     * @param context  上下文对象
     * @param filePath 文件路径
     * @return 返回一个文件
     */
    public static File getDiskCacheDir(Context context, String filePath) {
        boolean externalStorageAvailable = Environment
                .getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + filePath);
    }

    public static ImageResizer getImageResizer() {
        return imageResizer;
    }

    public static void setImageResizer(ImageResizer imageResizer) {
        DiskLruCacheUtils.imageResizer = imageResizer;
    }

    public static DiskLruCache getDiskLruCache() {
        return diskLruCache;
    }

    public static void setDiskLruCache(DiskLruCache diskCache) {
        diskLruCache = diskCache;
    }
}

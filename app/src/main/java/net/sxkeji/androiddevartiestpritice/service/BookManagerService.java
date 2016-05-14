package net.sxkeji.androiddevartiestpritice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import net.sxkeji.androiddevartiestpritice.Book;
import net.sxkeji.androiddevartiestpritice.IBookManager;
import net.sxkeji.androiddevartiestpritice.IOnNewBookListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AIDL Server
 * Created by zhangshixin on 3/19/2016.
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookListener> mListenerList = new RemoteCallbackList<>();
    private AtomicBoolean isServiceDestoryed = new AtomicBoolean(false);       //用于终止连接的标志

    private Binder mBinder = new IBookManager.Stub(){
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.e(TAG, "server getBookList");
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.e(TAG,"server addBook :" + book.toString());
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookListener listener) throws RemoteException {
            //RemoteCallBack完美封装了接口的 注册和取消
            mListenerList.register(listener);

//            if(mListenerList.contains(listener)){
//                Log.e(TAG,"listener already exists!");
//            }else{
//                mListenerList.add(listener);
//                Log.e(TAG, "listener's size : " + mListenerList.size());
//            }
        }

        @Override
        public void unRegisterListener(IOnNewBookListener listener) throws RemoteException {
            mListenerList.unregister(listener);

//            if(mListenerList.contains(listener)){
//                mListenerList.remove(listener);
//                Log.e(TAG, "listener's size : " + mListenerList.size());
//            }else {
//                Log.e(TAG,"listener not found!");
//            }
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "aaaaaaaaaa"));
        mBookList.add(new Book(2, "bbbbbbbbbb"));

        new Thread(new ServiceWork()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        isServiceDestoryed.set(true);
        super.onDestroy();
    }

    /**
     * 新书到达时通知所有监听器
     * @param book
     * @throws RemoteException
     */
    private void onNewBookArrived(Book book) throws RemoteException {
        Log.e(TAG, "onNewBookArrived , notify listeners" + mListenerList.toString());
        mBookList.add(book);

        final int N = mListenerList.beginBroadcast();
        for (int i = 0 ; i < N ; i ++){
            IOnNewBookListener item = mListenerList.getBroadcastItem(i);
            if(item != null){
                item.onNewBook(book);
            }
        }
        mListenerList.finishBroadcast();

//        for (int i = 0 ; i < mListenerList.size() ; i++){
//            IOnNewBookListener onNewBookListener = mListenerList.get(i);
//            if(onNewBookListener != null) {
//                onNewBookListener.onNewBook(book);
//            }
//        }
    }

    /**
     * 模拟每隔3秒新书到达
     */
    private class ServiceWork implements Runnable {
        @Override
        public void run() {
            while (!isServiceDestoryed.get()){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int newBookId = mBookList.size() + 1;
                Book newBook = new Book(newBookId,"book#" + newBookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

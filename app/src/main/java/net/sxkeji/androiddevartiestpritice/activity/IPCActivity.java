package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.Book;
import net.sxkeji.androiddevartiestpritice.Constants;
import net.sxkeji.androiddevartiestpritice.IBookManager;
import net.sxkeji.androiddevartiestpritice.IOnNewBookListener;
import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.service.BookManagerService;

import java.util.List;

/**
 * Android IPC Practice                 <br />
 * Created by zhangshixin on 3/18/2016. <br />
 * Email : sxzhang2016@163.com
 */
public class IPCActivity extends Activity {
    private static final String TAG = IPCActivity.class.getSimpleName();
    private static final int MSG_NEW_BOOK = 1;

    private IBookManager mRemoteBookManager;
    private Button btn_send_msg, btn_send_aidl, btn_send_socket;
    private Messenger mMessengerToService;
    private Messenger mMessengerReply = new Messenger(new MessengerHandler());
    private boolean isRegistered = false;

    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessengerToService = new Messenger(service);
            Message msg = Message.obtain(null, Constants.MSG_FROM_CLIENT);
            Bundle data = new Bundle();         //存放数据的关键
            data.putString("msg", "Hello , I'm the client!");
            msg.setData(data);
            //添加对服务端回复消息的接收
            msg.replyTo = mMessengerReply;
            try {
                mMessengerToService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, "error :" + e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceConnection mAIDLConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = bookManager;
            try {
                List<Book> bookList = bookManager.getBookList();
//                Log.e(TAG,"query book list , list type : "+ bookList.getClass().getCanonicalName());
                Log.e(TAG, "old book list : " + bookList.toString());
                bookManager.addBook(new Book(3, "the story of zsx"));
                List<Book> newList = bookManager.getBookList();
                Log.e(TAG, "new book list : " + newList.toString());

                //注册新书监听
                mRemoteBookManager.registerListener(mIOnNewBookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.e(TAG, "Binder died!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
    }


    private void initViews() {
        btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        btn_send_aidl = (Button) findViewById(R.id.btn_send_aidl);
        btn_send_socket = (Button) findViewById(R.id.btn_send_socket);
    }

    private void setListeners() {
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgToService();
            }
        });

        btn_send_aidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgByAIDL();
            }
        });
    }

    /**
     * 实现接口
     */
    IOnNewBookListener mIOnNewBookListener = new IOnNewBookListener.Stub() {
        @Override
        public void onNewBook(Book newBook) throws RemoteException {
            mAIDLHandler.obtainMessage(MSG_NEW_BOOK, newBook).sendToTarget();
        }
    };

    /**
     * 使用AIDL给服务端发消息
     */
    private void sendMsgByAIDL() {
        Intent sendIntent = new Intent(this, BookManagerService.class);
        boolean bindService = bindService(sendIntent, mAIDLConnection, Context.BIND_AUTO_CREATE);
        isRegistered = true;
        if (bindService) {
            Log.e(TAG, "bind service success!");
        } else {
            Log.e(TAG, "bind service failed!");
        }
    }

    /**
     * 使用Messenger给服务端发消息
     */
    private void sendMsgToService() {
//        Intent sendIntent = new Intent(this,MessengerService.class);
//        boolean bindService = bindService(sendIntent, mMessengerConnection, Context.BIND_AUTO_CREATE);
//        if(!bindService){
//            Log.e(TAG, "bind service failed!");
//        }else {
//            Log.e(TAG, "bind service success!");
//        }
    }


    @Override
    protected void onDestroy() {
//        unbindService(mMessengerConnection);
        if (isRegistered) {
            unbindService(mAIDLConnection);
            if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
                try {
                    mRemoteBookManager.unRegisterListener(mIOnNewBookListener);
                    Log.e(TAG, "unRegister listener " + mIOnNewBookListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }

        super.onDestroy();
    }


    private static final class MessengerHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MSG_FROM_SERVER:
                    Log.e(TAG, "Server reply: " + msg.getData().get("reply"));
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Handler mAIDLHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEW_BOOK:
                    Log.e(TAG, "client get the news of new book");
                    Toast.makeText(getApplicationContext(), "new book" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

}

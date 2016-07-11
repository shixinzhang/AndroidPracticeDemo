package net.sxkeji.androiddevartiestpritice.activity.eventbus;

/**
 * EventBus要发送的消息实体类
 * <p/>
 * Created by zhangshixin on 7/8/2016.
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

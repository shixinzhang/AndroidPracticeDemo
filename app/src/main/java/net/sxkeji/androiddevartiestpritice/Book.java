package net.sxkeji.androiddevartiestpritice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangshixin on 3/19/2016.
 */
public class Book implements Parcelable{
    private int _id;
    private String name;

    public Book(int i , String n){
        this._id = i;
        this.name = n;
    }

    @Override
    public String toString() {
        return "[id : "+ this._id + "; name :" + this.name + "]";
    }

    protected Book(Parcel in) {
        _id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(name);
    }
}

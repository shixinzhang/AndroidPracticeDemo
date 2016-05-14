// IBookManager.aidl
package net.sxkeji.androiddevartiestpritice;

// Declare any non-default types here with import statements
import net.sxkeji.androiddevartiestpritice.Book;

import net.sxkeji.androiddevartiestpritice.IOnNewBookListener;

interface IBookManager {
    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(IOnNewBookListener listener);

    void unRegisterListener(IOnNewBookListener listener);
}

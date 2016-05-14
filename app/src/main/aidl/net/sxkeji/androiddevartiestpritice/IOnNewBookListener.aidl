// IOnNewBookListener.aidl
package net.sxkeji.androiddevartiestpritice;

// Declare any non-default types here with import statements
import net.sxkeji.androiddevartiestpritice.Book;

interface IOnNewBookListener {
    void onNewBook(in Book newBook);
}

package pl.marcinlipinski.matchquizapp;
import android.content.ContentValues;

public interface Service<T> {
    void initialize();
    ContentValues newContent(T object);
}

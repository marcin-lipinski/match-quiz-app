package pl.marcinlipinski.matchquizapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public interface DatabaseContext {
    void createTable(String query);
    void save(String tableName, ContentValues content);
    SQLiteDatabase read();
}

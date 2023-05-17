package pl.marcinlipinski.matchquizapp.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public interface DatabaseContext {
    void query(String query);

    void save(String tableName, ContentValues content);

    SQLiteDatabase read();
}

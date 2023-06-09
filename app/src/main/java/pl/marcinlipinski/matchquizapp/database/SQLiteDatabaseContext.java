package pl.marcinlipinski.matchquizapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import lombok.Getter;

import javax.inject.Inject;

@Getter

public class SQLiteDatabaseContext extends SQLiteOpenHelper implements DatabaseContext {
    @Inject
    public SQLiteDatabaseContext(@Nullable Context context) {
        super(context, "matchquizdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public void query(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    @Override
    public void save(String tableName, ContentValues content) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, content);
    }

    @Override
    public SQLiteDatabase read() {
        return getReadableDatabase();
    }
}

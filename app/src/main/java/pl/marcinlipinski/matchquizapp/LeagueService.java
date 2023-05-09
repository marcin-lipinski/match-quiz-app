package pl.marcinlipinski.matchquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LeagueService extends SQLiteOpenHelper {
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String LOGO = "LOGO";
    private static final String TABLE_NAME = "LEAGUE_TABLE";

    public LeagueService(@Nullable Context context) {
        super(context, "matchquizdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + LOGO + " TEXT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void initializeDatabase(){
        File file = new File("//data//data//pl.marcinlipinski.matchquizapp//databases//matchquizdb");
        if(file.exists()) return;
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, newContent(191, "Ekstraklasa", "https://tipsscore.com/resb/league/poland-ekstraklasa.png"));
        db.insert(TABLE_NAME, null, newContent(251,"LaLiga","https://tipsscore.com/resb/league/spain-laliga.png"));
        db.insert(TABLE_NAME, null, newContent(317, "Premier League","https://tipsscore.com/resb/league/england-premier-league.png"));
        db.insert(TABLE_NAME, null, newContent(498, "Ligue 1", "https://tipsscore.com/resb/league/france-ligue-1.png"));
        db.insert(TABLE_NAME, null, newContent(512, "Bundesliga", "https://tipsscore.com/resb/league/germany-bundesliga.png"));
        db.insert(TABLE_NAME, null, newContent(592,"Serie A", "https://tipsscore.com/resb/league/italy-serie-a.png"));
        db.insert(TABLE_NAME, null, newContent(817,"UEFA Champions League", "https://tipsscore.com/resb/league/europe-uefa-champions-league.png"));
        db.insert(TABLE_NAME, null, newContent(818,"UEFA Europa League", "https://tipsscore.com/resb/league/europe-uefa-europa-league.png"));
        db.insert(TABLE_NAME, null, newContent(8911, "UEFA Europa Conference League", "https://tipsscore.com/resb/league/europe-uefa-europa-conference-league.png"));
    }

    public List<League> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<League> leagues = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                leagues.add(new League(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return leagues;
    }

    private ContentValues newContent(int id, String name, String logo){
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(NAME, name);
        cv.put(LOGO, logo);
        return cv;
    }
}

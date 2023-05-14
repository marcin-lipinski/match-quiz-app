package pl.marcinlipinski.matchquizapp.servicies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Approach;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ApproachService implements Service<Approach> {
    private final SQLiteDatabaseContext databaseContext;
    public ApproachService(SQLiteDatabaseContext databaseContext){
        this.databaseContext = databaseContext;
    }
    @Override
    public void initialize() {
        databaseContext.createTable("CREATE TABLE if not exists APPROACH_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            "LEAGUE TEXT, APPROACH_TIME TEXT, SCORE TEXT, FAVOURITE INTEGER)");
    }

    public ArrayList<Approach> getAllApproaches(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = databaseContext.read();
        Cursor cursor = db.rawQuery("SELECT * FROM APPROACH_TABLE", null);
        ArrayList<Approach> approaches = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                approaches.add(new Approach(
                        cursor.getLong(0),
                        cursor.getString(1),
                        LocalDate.parse(cursor.getString(2), formatter),
                        cursor.getInt(3),
                        cursor.getInt(4)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return approaches;
    }

    public void setApproachFavourite(Long approachId, int value){
        databaseContext.createTable("UPDATE APPROACH_TABLE SET FAVOURITE = " + value + " WHERE ID = " + approachId);
    }

    public void deleteApproach(Long approachId){
        databaseContext.createTable("DELETE FROM APPROACH_TABLE WHERE ID = " + approachId);
    }

    @Override
    public ContentValues newContent(Approach approach) {
        ContentValues cv = new ContentValues();
        cv.put("ID", approach.getId());
        cv.put("LEAGUE", approach.getLeague());
        cv.put("APPROACH_TIME", approach.getApproachDate().toString());
        cv.put("SCORE", approach.getScore());
        cv.put("FAVOURITE", approach.getFavourite());
        return cv;
    }
}

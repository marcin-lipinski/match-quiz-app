package pl.marcinlipinski.matchquizapp.servicies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import pl.marcinlipinski.matchquizapp.database.DatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Approach;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ApproachService implements Service<Approach> {
    @Inject
    DatabaseContext databaseContext;
    private static Approach approach;

    @Inject
    public ApproachService(DatabaseContext databaseContext){
        this.databaseContext = databaseContext;
        this.initialize();
    }
    @Override
    public void initialize() {
        databaseContext.query("CREATE TABLE if not exists APPROACH_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "LEAGUE TEXT, SEASON TEXT, APPROACH_TIME TEXT, SCORE TEXT, FAVOURITE INTEGER)");
    }

    public static Approach getTemporaryApproach(){return ApproachService.approach;}
    public static void resetTemporaryApproach(){ApproachService.approach = new Approach();}

    public ArrayList<Approach> getAllApproaches(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SQLiteDatabase db = databaseContext.read();
        Cursor cursor = db.rawQuery("SELECT * FROM APPROACH_TABLE", null);
        ArrayList<Approach> approaches = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                approaches.add(new Approach(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        LocalDate.parse(cursor.getString(3), formatter),
                        cursor.getInt(4),
                        cursor.getInt(5)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return approaches;
    }

    public void setApproachFavourite(Long approachId, int value){
        databaseContext.query("UPDATE APPROACH_TABLE SET FAVOURITE = " + value + " WHERE ID = " + approachId);
    }

    public void deleteApproach(Long approachId){
        databaseContext.query("DELETE FROM APPROACH_TABLE WHERE ID = " + approachId);
    }

    public void saveGame(){
        ApproachService.approach.setFavourite(0);
        databaseContext.save("APPROACH_TABLE", newContent(ApproachService.approach));
    }

    @Override
    public ContentValues newContent(Approach approach) {
        ContentValues cv = new ContentValues();
        cv.put("ID", approach.getId());
        cv.put("LEAGUE", approach.getLeague());
        cv.put("SEASON", approach.getSeason());
        cv.put("APPROACH_TIME", approach.getApproachDate().toString());
        cv.put("SCORE", approach.getScore());
        cv.put("FAVOURITE", approach.getFavourite());
        return cv;
    }
}

package pl.marcinlipinski.matchquizapp;

import android.content.ContentValues;

public class ApproachService implements Service<Approach>{
    private final SQLiteDatabaseContext databaseContext;
    public ApproachService(SQLiteDatabaseContext databaseContext){
        this.databaseContext = databaseContext;
    }
    @Override
    public void initialize() {
        databaseContext.createTable("CREATE TABLE if not exists APPROACH_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            "LEAGUE TEXT, APPROACH_TIME TEXT, SCORE TEXT, FAVOURITE INTEGER)");
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

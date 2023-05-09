package pl.marcinlipinski.matchquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaguesService implements Service<League>{
    private final DatabaseContext databaseContext;
    public LeaguesService(DatabaseContext databaseContext){
        this.databaseContext = databaseContext;
    }

    @Override
    public void initialize(){
        File file = new File("//data//data//pl.marcinlipinski.matchquizapp//databases//matchquizdb");
        if(file.exists()) return;

        databaseContext.createTable("CREATE TABLE LEAGUE_TABLE (ID INTEGER PRIMARY KEY, NAME TEXT, LOGO TEXT)");
        save(new League(191L, "Ekstraklasa", "https://tipsscore.com/resb/league/poland-ekstraklasa.png"));
        save(new League(251L,"LaLiga","https://tipsscore.com/resb/league/spain-laliga.png"));
        save(new League(317L, "Premier League","https://tipsscore.com/resb/league/england-premier-league.png"));
        save(new League(498L, "Ligue 1", "https://tipsscore.com/resb/league/france-ligue-1.png"));
        save(new League(512L, "Bundesliga", "https://tipsscore.com/resb/league/germany-bundesliga.png"));
        save(new League(592L,"Serie A", "https://tipsscore.com/resb/league/italy-serie-a.png"));
        save(new League(817L,"UEFA Champions League", "https://tipsscore.com/resb/league/europe-uefa-champions-league.png"));
        save(new League(818L,"UEFA Europa League", "https://tipsscore.com/resb/league/europe-uefa-europa-league.png"));
        save(new League(8911L, "UEFA Europa Conference League", "https://tipsscore.com/resb/league/europe-uefa-europa-conference-league.png"));
    }

    public void save(League league){
        databaseContext.save("LEAGUE_TABLE", newContent(league));
    }

    public List<League> getAll(){
        SQLiteDatabase db = databaseContext.read();
        Cursor cursor = db.rawQuery("SELECT * FROM LEAGUE_TABLE", null);
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

    public void getSeasonsByLeagueId(Long leagueId, Context context, final VolleyCallback volleyCallback){
        String url = "https://sportscore1.p.rapidapi.com/leagues/" + leagueId + "/seasons";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        JSONArray json = response.getJSONArray("data");
                        HashMap<Long, String> seasons = new HashMap<>();
                        for(int i = 0 ; i < json.length(); i++){
                            Long id = json.getJSONObject(i).getLong("id");
                            String name = json.getJSONObject(i).getString("name");
                            seasons.put(id, name);
                        }
                        volleyCallback.onSuccess(seasons);
                    } catch (JSONException e) {
                        volleyCallback.onFail();
                    }
                },
                error -> {
                    Log.d("ERROR","error => "+error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Key", "bef5e777cemsh913f1631d392ffcp18bd9ejsn8198ee1dbb09");
                params.put("X-RapidAPI-Host", "sportscore1.p.rapidapi.com");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    @Override
    public ContentValues newContent(League league){
        ContentValues cv = new ContentValues();
        cv.put("ID", league.getId());
        cv.put("NAME", league.getName());
        cv.put("LOGO", league.getLogo());
        return cv;
    }
}

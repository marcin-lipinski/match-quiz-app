package pl.marcinlipinski.matchquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class EventService implements Service<Event>{
    private final DatabaseContext databaseContext;
    public EventService(DatabaseContext databaseContext){
        this.databaseContext = databaseContext;
    }

    public void initialize(){
        databaseContext.createTable("CREATE TABLE EVENT_TABLE (ID INTEGER PRIMARY KEY, " +
                                            "HOME_TEAM TEXT, AWAY_TEAM TEXT, HOME_TEAM_LOGO TEXT, AWAY_TEAM_LOGO TEXT, " +
                                            "HOME_TEAM_SCORE TEXT, AWAY_TEAM_SCORE TEXT, START_TIME TEXT)");
    }

    @Override
    public ContentValues newContent(Event event) {
        ContentValues cv = new ContentValues();
        cv.put("ID", event.getId());
        cv.put("HOME_TEAM", event.getHomeTeam());
        cv.put("AWAY_TEAM", event.getAwayTeam());
        cv.put("HOME_TEAM_LOGO", event.getHomeTeamLogo());
        cv.put("AWAY_TEAM_LOGO", event.getAwayTeamLogo());
        cv.put("HOME_TEAM_SCORE", event.getAwayTeamScore());
        cv.put("AWAY_TEAM_SCORE", event.getAwayTeamScore());
        cv.put("START_TIME", event.getStartTime().toString());
        return cv;
    }


    public void getEventsBySeasonId(Long seasonId, Context context, final VolleyCallback volleyCallback){
        String url = "https://sportscore1.p.rapidapi.com/seasons/" + seasonId + "/events";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        JSONArray json = response.getJSONArray("data");
                        HashMap<Long, Event> matches = new HashMap<>();
                        for(int i = 0 ; i < json.length(); i++){
                            JSONObject data = json.getJSONObject(i);
                            if(!data.getString("status").equals("finished")) continue;
                            Event event = Event.builder()
                                            .id(data.getLong("id"))
                                            .homeTeam(data.getJSONObject("home_team").getString("name_full"))
                                            .awayTeam(data.getJSONObject("away_team").getString("name_full"))
                                            .homeTeamLogo(data.getJSONObject("home_team").getString("logo"))
                                            .awayTeamLogo(data.getJSONObject("away_team").getString("logo"))
                                            .homeTeamScore(data.getJSONObject("home_score").getInt("normal_time"))
                                            .awayTeamScore(data.getJSONObject("away_score").getInt("normal_time"))
                                            .startTime(LocalDate.parse(data.getString("start_at"), formatter)).build();

                            matches.put(data.getLong("id"), event);
                        }
                        volleyCallback.onSuccess(matches);
                    } catch (JSONException e) {
                        Log.d("ERROR",e.getMessage());
                        volleyCallback.onFail();
                    }
                },
                error -> {
                    volleyCallback.onFail();
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
}

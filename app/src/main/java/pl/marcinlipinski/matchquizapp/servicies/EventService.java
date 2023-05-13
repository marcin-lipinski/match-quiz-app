package pl.marcinlipinski.matchquizapp.servicies;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.marcinlipinski.matchquizapp.database.DatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventService implements Service<Event> {
    private final DatabaseContext databaseContext;
    public EventService(DatabaseContext databaseContext){
        this.databaseContext = databaseContext;
    }

    public void initialize(){
        databaseContext.createTable("CREATE TABLE if not exists EVENT_TABLE (ID INTEGER PRIMARY KEY, " +
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


    public void getEventsBySeasonId(Long seasonId, Context context, VolleyCallback volleyCallback){
        String url = "https://sportscore1.p.rapidapi.com/seasons/" + seasonId + "/events";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        JSONArray json = response.getJSONArray("data");
                        ArrayList<Event> events = new ArrayList<>();
                        for(int i = 0 ; i < json.length(); i++){
                            JSONObject data = json.getJSONObject(i);
                            if(!data.getString("status").equals("finished")) continue;
                            Event event = Event.builder()
                                            .id(data.getLong("id"))
                                            .homeTeamId(data.getJSONObject("home_team").getLong("id"))
                                            .awayTeamId(data.getJSONObject("away_team").getLong("id"))
                                            .homeTeam(data.getJSONObject("home_team").getString("name_full"))
                                            .awayTeam(data.getJSONObject("away_team").getString("name_full"))
                                            .homeTeamLogo(data.getJSONObject("home_team").getString("logo"))
                                            .awayTeamLogo(data.getJSONObject("away_team").getString("logo"))
                                            .homeTeamScore(data.getJSONObject("home_score").getInt("normal_time"))
                                            .awayTeamScore(data.getJSONObject("away_score").getInt("normal_time"))
                                            .winnerCode(data.getInt("winner_code"))
                                            .startTime(LocalDate.parse(data.getString("start_at"), formatter)).build();

                            events.add(event);
                        }
                        volleyCallback.onSuccess(events);
                    } catch (JSONException e) {
                        volleyCallback.onFail(e.getMessage());
                    }
                },
                error -> {
                    volleyCallback.onFail(error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Key", "04f771aac6mshe49043b94d7e752p1e5388jsn4490c1fd12b3");
                params.put("X-RapidAPI-Host", "sportscore1.p.rapidapi.com");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

    public void getCityDistanceByWinnerTeamId(Long teamId, Context context, VolleyCallback volleyCallback){
        String url = "https://sportscore1.p.rapidapi.com/teams/" + teamId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        String city = data.getJSONObject("venue").getJSONObject("city").getString("en");

                        volleyCallback.onSuccess(city);
                    } catch (JSONException e) {
                        volleyCallback.onFail(e.getMessage());
                    }
                },
                error -> {
                    volleyCallback.onFail(error.getMessage());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Key", "04f771aac6mshe49043b94d7e752p1e5388jsn4490c1fd12b3");
                params.put("X-RapidAPI-Host", "sportscore1.p.rapidapi.com");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }
}

package pl.marcinlipinski.matchquizapp;

import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseContext databaseContext = new DatabaseContext(MainActivity.this);
        LeaguesService leaguesService = new LeaguesService(databaseContext);
        EventService eventService = new EventService(databaseContext);

        TextView view = (TextView) findViewById(R.id.text2);
        final String[] r = {""};
        leaguesService.getAll().forEach(m -> r[0] += m.getName() + "\n");
        view.setText(r[0]);

        leaguesService.getSeasonsByLeagueId(317L, this, new VolleyCallback<HashMap<Long, String>>() {
            @Override
            public void onSuccess(HashMap<Long, String> result) {
                for(Long key : result.keySet() ){
                    Log.d(String.valueOf(key), result.get(key));
                }
            }
            @Override
            public void onFail() {

            }
        });

        eventService.getEventsBySeasonId(8960L, this, new VolleyCallback<HashMap<Long, Event>>() {
            @Override
            public void onSuccess(HashMap<Long, Event> result) {
                for(Long key : result.keySet() ){
                    Log.d(String.valueOf(key), result.get(key).getHomeTeam());
                }
            }

            @Override
            public void onFail() {

            }
        });
    }
}
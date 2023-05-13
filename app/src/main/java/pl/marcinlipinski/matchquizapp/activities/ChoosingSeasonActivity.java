package pl.marcinlipinski.matchquizapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.RecyclerViewInterface;
import pl.marcinlipinski.matchquizapp.SeasonsRecycleViewAdapter;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Season;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;
import pl.marcinlipinski.matchquizapp.servicies.VolleyCallback;

import java.util.ArrayList;
import java.util.List;

public class ChoosingSeasonActivity extends Activity implements RecyclerViewInterface {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosing_season);

        Intent intent = getIntent();
        String name = intent.getStringExtra("leagueName");
        Long id = intent.getLongExtra("leagueId", -1);

        SQLiteDatabaseContext databaseContext = new SQLiteDatabaseContext(this);
        LeaguesService leaguesService = new LeaguesService(databaseContext);


        TextView v = (TextView)findViewById(R.id.text22);
        v.setText(name);

        leaguesService.getSeasonsByLeagueId(id, this, new VolleyCallback<ArrayList<Season>>() {
            @Override
            public void onSuccess(ArrayList<Season> seasons) {
                List<Long> ids = new ArrayList<>();
                List<String> names = new ArrayList<>();
                for(Season season : seasons){
                    Log.d(season.getName(), String.valueOf(season.getId()));

                    ids.add(season.getId());
                    names.add(season.getName());

                    RecyclerView recyclerView = findViewById(R.id.seasons_recycleview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChoosingSeasonActivity.this));
                    recyclerView.setAdapter(new SeasonsRecycleViewAdapter(getApplicationContext(), names, ids, ChoosingSeasonActivity.this));

                }
            }
            @Override
            public void onFail(String message) {
                Log.d("Season by Id", message);
            }
        });
    }

    @Override
    public void onItemClick(Long id) {
        Log.d("Sezon", String.valueOf(id));
        Intent myIntent = new Intent(this, QuizActivity.class);
        myIntent.putExtra("seasonId", id);
        startActivity(myIntent);
    }
}

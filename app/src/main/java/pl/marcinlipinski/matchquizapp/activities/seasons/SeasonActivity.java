package pl.marcinlipinski.matchquizapp.activities.seasons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.activities.quiz.QuizActivity;
import pl.marcinlipinski.matchquizapp.dependecyInjection.AppInjector;
import pl.marcinlipinski.matchquizapp.models.Season;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;
import pl.marcinlipinski.matchquizapp.servicies.VolleyCallback;

import javax.inject.Inject;
import java.util.ArrayList;

public class SeasonActivity extends AppCompatActivity implements RecyclerViewInterface {
    @Inject
    LeaguesService leaguesService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);
        AppInjector.inject(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("leagueName");
        Long id = intent.getLongExtra("leagueId", -1);

        ApproachService.resetTemporaryApproach();
        ApproachService.getTemporaryApproach().setLeague(name);

        leaguesService.getSeasonsByLeagueId(id, this, new VolleyCallback<ArrayList<Season>>() {
            @Override
            public void onSuccess(ArrayList<Season> seasons) {
                ((TextView)findViewById(R.id.league_name)).setText(name);
                RecyclerView recyclerView = findViewById(R.id.seasons_recycleview);
                recyclerView.setLayoutManager(new LinearLayoutManager(SeasonActivity.this));
                recyclerView.setAdapter(new SeasonsRecycleViewAdapter(getApplicationContext(), seasons, SeasonActivity.this));
            }
            @Override
            public void onFail(String message) {
                Log.d("Season by Id", message);
            }
        });
    }

    @Override
    public void onItemClick(Long id) {
        Intent myIntent = new Intent(this, QuizActivity.class);
        myIntent.putExtra("seasonId", id);
        startActivity(myIntent);
    }
}

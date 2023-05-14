package pl.marcinlipinski.matchquizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.marcinlipinski.matchquizapp.LeagueGridViewAdapter;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.League;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;

import java.util.ArrayList;


public class PlayFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gview = inflater.inflate(R.layout.fragment_play, container, false);

        SQLiteDatabaseContext databaseContext = new SQLiteDatabaseContext(getActivity());
        LeaguesService leaguesService = new LeaguesService(databaseContext);
        leaguesService.initialize();
        EventService eventService = new EventService(databaseContext);
        ApproachService approachService = new ApproachService(databaseContext);
        eventService.initialize();


        ArrayList<League> leagues = leaguesService.getAll();

        GridView gridView;
        gridView = gview.findViewById(R.id.leaguesGridView);
        gridView.setAdapter(new LeagueGridViewAdapter(getActivity(), leagues));

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent myIntent = new Intent(getActivity(), ChoosingSeasonActivity.class);
            myIntent.putExtra("leagueName", leagues.get(i).getName());
            myIntent.putExtra("leagueId", leagues.get(i).getId());
            startActivity(myIntent);
        });

        return gview;
    }
}
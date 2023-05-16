package pl.marcinlipinski.matchquizapp.activities.main.play;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.activities.seasons.SeasonActivity;
import pl.marcinlipinski.matchquizapp.models.League;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;

import javax.inject.Inject;
import java.util.ArrayList;


public class PlayFragment extends Fragment {
    @Inject
    LeaguesService leaguesService;
    ArrayList<League> leagues;

    @Inject
    public PlayFragment(LeaguesService leaguesService) {
        this.leaguesService = leaguesService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentPlayView = inflater.inflate(R.layout.fragment_play, container, false);

        leagues = leaguesService.getAll();
        GridView gridView = fragmentPlayView.findViewById(R.id.leaguesGridView);
        gridView.setAdapter(new LeaguesGridViewAdapter(getActivity(), leagues));

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent myIntent = new Intent(getActivity(), SeasonActivity.class);
            myIntent.putExtra("leagueName", leagues.get(i).getName());
            myIntent.putExtra("leagueId", leagues.get(i).getId());
            startActivity(myIntent);
        });

        return fragmentPlayView;
    }
}
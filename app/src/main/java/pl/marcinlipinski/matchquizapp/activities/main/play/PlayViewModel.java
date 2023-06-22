package pl.marcinlipinski.matchquizapp.activities.main.play;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.marcinlipinski.matchquizapp.models.League;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;

import javax.inject.Inject;
import java.util.ArrayList;

public class PlayViewModel extends ViewModel {
    private LeaguesService leaguesService;
    private MutableLiveData<ArrayList<League>> leaguesLiveData;

    public PlayViewModel() {}

    @Inject
    public PlayViewModel(LeaguesService leaguesService) {
        this.leaguesService = leaguesService;
        leaguesLiveData = new MutableLiveData<>();
    }

    public LiveData<ArrayList<League>> getLeaguesLiveData() {
        return leaguesLiveData;
    }

    public void loadLeagues() {
        ArrayList<League> leagues = leaguesService.getAll();
        leaguesLiveData.setValue(leagues);
    }
}

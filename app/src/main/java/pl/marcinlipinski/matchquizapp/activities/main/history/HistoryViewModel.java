package pl.marcinlipinski.matchquizapp.activities.main.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.marcinlipinski.matchquizapp.models.Approach;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;

public class HistoryViewModel extends ViewModel {
    private ApproachService approachService;
    private MutableLiveData<ArrayList<Approach>> approachesLiveData;

    public HistoryViewModel() {
    }

    @Inject
    public HistoryViewModel(ApproachService approachService) {
        this.approachService = approachService;
        approachesLiveData = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Approach>> getApproachesLiveData() {
        return approachesLiveData;
    }

    public void loadApproaches() {
        ArrayList<Approach> approaches = approachService.getAllApproaches();
        approaches.sort(getApproachSort());
        approachesLiveData.setValue(approaches);
    }

    public void setApproachFavourite(Approach approach, int favourite) {
        approachService.setApproachFavourite(approach.getId(), favourite);
        loadApproaches();
    }

    public void deleteApproach(Approach approach) {
        approachService.deleteApproach(approach.getId());
        loadApproaches();
    }

    public Comparator<Approach> getApproachSort() {
        return (approachA, approachB) -> {
            if (approachA.getFavourite() > approachB.getFavourite()) return -1;
            if (approachA.getFavourite() < approachB.getFavourite()) return 1;
            if (approachA.getApproachDate().isAfter(approachB.getApproachDate())) return -1;
            if (approachA.getApproachDate().isBefore(approachB.getApproachDate())) return 1;
            return 0;
        };
    }
}
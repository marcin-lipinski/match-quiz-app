package pl.marcinlipinski.matchquizapp.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.ApproachesRecycleViewInterface;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.Approach;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class HistoryFragment extends Fragment implements ApproachesRecycleViewInterface {
    @Inject
    ApproachService approachService;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ApproachesRecycleViewAdapter approachesRecycleViewAdapter;
    static ArrayList<Approach> approaches;
    static boolean ap = true;

    @Inject
    public HistoryFragment(ApproachService approachService){
        this.approachService = approachService;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentHistory =  inflater.inflate(R.layout.fragment_history, container, false);

        approaches = approachService.getAllApproaches();

        recyclerView = fragmentHistory.findViewById(R.id.history_recycleview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        approachesRecycleViewAdapter = new ApproachesRecycleViewAdapter(getActivity(), approaches, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(approachesRecycleViewAdapter);

        return fragmentHistory;
    }

    public Comparator<Approach> approachSort = (approachA, approachB) -> {
        if(approachA.getFavourite() > approachB.getFavourite()) return -1;
        if(approachA.getFavourite() < approachB.getFavourite()) return 1;
        if(approachA.getApproachDate().isAfter(approachB.getApproachDate())) return -1;
        if(approachA.getApproachDate().isBefore(approachB.getApproachDate())) return 1;
        return 0;
    };

    @Override
    public void onFavouriteButtonClick(ImageButton button, int approachPos) {
        Approach approach = approaches.get(approachPos);

        if(approach.getFavourite() == 1){
            approachService.setApproachFavourite(approach.getId(), 0);
            button.setImageResource(R.drawable.baseline_favorite_black_icon);

            approach.setFavourite(0);
        }
        else{
            approachService.setApproachFavourite(approach.getId(), 1);
            button.setImageResource(R.drawable.baseline_favorite_red_icon);

            approach.setFavourite(1);
        }
        approaches.sort(approachSort);
        approachesRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteButtonClick(ImageButton button, int approachPos) {
        approachService.deleteApproach(approaches.get(approachPos).getId());
        ApproachesRecycleViewAdapter.approaches.remove(approachPos);
        approachesRecycleViewAdapter.notifyDataSetChanged();
    }
}
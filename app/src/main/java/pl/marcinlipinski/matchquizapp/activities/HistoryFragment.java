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
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Approach;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class HistoryFragment extends Fragment implements ApproachesRecycleViewInterface {
    static ArrayList<Approach> approaches;
    ApproachService approachService;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ApproachesRecycleViewAdapter approachesRecycleViewAdapter;
    static boolean ap = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history, container, false);

        SQLiteDatabaseContext databaseContext = new SQLiteDatabaseContext(getActivity());

        approachService = new ApproachService(databaseContext);
        approachService.initialize();


        if(ap){
            approaches = approachService.getAllApproaches();
            approaches.add(Approach.builder().id(1L).approachDate(LocalDate.now()).favourite(1).league("UEFA Europe Conference League").score(4).build());
            approaches.add(Approach.builder().id(2L).approachDate(LocalDate.now()).favourite(0).league("UEFA Europe Conference League").score(3).build());
            approaches.add(Approach.builder().id(3L).approachDate(LocalDate.now().minusMonths(1)).favourite(1).league("UEFA Europe Conference League").score(5).build());
            ap = false;
        }
        recyclerView = view.findViewById(R.id.history_recycleview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        approachesRecycleViewAdapter = new ApproachesRecycleViewAdapter(getActivity(), approaches, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(approachesRecycleViewAdapter);

        return view;
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
            button.setImageResource(R.drawable.baseline_favorite_black);

            approach.setFavourite(0);
        }
        else{
            approachService.setApproachFavourite(approach.getId(), 1);
            button.setImageResource(R.drawable.baseline_favorite_red);

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
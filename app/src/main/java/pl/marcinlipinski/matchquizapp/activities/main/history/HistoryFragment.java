package pl.marcinlipinski.matchquizapp.activities.main.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.Approach;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;

import javax.inject.Inject;
import java.util.ArrayList;

public class HistoryFragment extends Fragment implements HistoryRecycleViewInterface {
    @Inject
    ApproachService approachService;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    HistoryRecycleViewAdapter approachesRecycleViewAdapter;
    ArrayList<Approach> approaches;

    private HistoryViewModel historyViewModel;

    @Inject
    public HistoryFragment(ApproachService approachService) {
        this.approachService = approachService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentHistory = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = fragmentHistory.findViewById(R.id.history_recycleview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        approachesRecycleViewAdapter = new HistoryRecycleViewAdapter(getActivity(), this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(approachesRecycleViewAdapter);

        loadApproaches();

        return fragmentHistory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadApproaches() {
        approaches = approachService.getAllApproaches();
        approaches.sort(historyViewModel.getApproachSort());
        approachesRecycleViewAdapter.setApproaches(approaches);
        approachesRecycleViewAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onFavouriteButtonClick(ImageButton button, int approachPos) {
        Approach approach = approaches.get(approachPos);

        int newFavouriteValue = (approach.getFavourite() == 1) ? 0 : 1;
        approachService.setApproachFavourite(approach.getId(), newFavouriteValue);

        approach.setFavourite(newFavouriteValue);
        approaches.sort(historyViewModel.getApproachSort());
        approachesRecycleViewAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteButtonClick(ImageButton button, int approachPos) {
        Approach approach = approaches.get(approachPos);
        approachService.deleteApproach(approach.getId());
        approaches.remove(approach);
        approachesRecycleViewAdapter.notifyDataSetChanged();
    }
}
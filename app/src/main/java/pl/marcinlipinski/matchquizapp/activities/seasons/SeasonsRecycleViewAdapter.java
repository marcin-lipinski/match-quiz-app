package pl.marcinlipinski.matchquizapp.activities.seasons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.Season;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;

import java.util.ArrayList;

public class SeasonsRecycleViewAdapter extends RecyclerView.Adapter<SeasonsRecycleViewAdapter.SeasonHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    static ArrayList<Season> seasons;

    public SeasonsRecycleViewAdapter(Context context, ArrayList<Season> seasons, RecyclerViewInterface recyclerViewInterface) {
        SeasonsRecycleViewAdapter.seasons = seasons;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public SeasonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_season, parent, false);
        return new SeasonHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonHolder holder, int position) {
        holder.seasonNameTextView.setText(seasons.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    public static class SeasonHolder extends RecyclerView.ViewHolder {
        TextView seasonNameTextView;

        public SeasonHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            seasonNameTextView = itemView.findViewById(R.id.season_name);
            itemView.setOnClickListener(view ->
            {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ApproachService.getTemporaryApproach().setSeason(seasons.get(position).getName());
                        recyclerViewInterface.onItemClick(seasons.get(position).getId());
                    }
                }
            });
        }
    }
}
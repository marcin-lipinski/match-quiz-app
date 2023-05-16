package pl.marcinlipinski.matchquizapp.activities.main.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.Approach;
import java.util.ArrayList;

public class HistoryRecycleViewAdapter extends RecyclerView.Adapter<HistoryRecycleViewAdapter.ApproachHolder>{
    static ArrayList<Approach> approaches;
    HistoryRecycleViewInterface recyclerViewInterface;
    Context context;

    public HistoryRecycleViewAdapter(Context context, ArrayList<Approach> approaches, HistoryRecycleViewInterface recyclerViewInterface){
        HistoryRecycleViewAdapter.approaches = approaches;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }
    @NonNull
    @Override
    public HistoryRecycleViewAdapter.ApproachHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycleview_history, parent, false);
        return new HistoryRecycleViewAdapter.ApproachHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecycleViewAdapter.ApproachHolder holder, int position) {
        Approach approach = approaches.get(position);
        holder.leagueNameTextView.setText(approach.getSeason());
        holder.appraochDateTextView.setText(approach.getApproachDate().toString());
        holder.approachScoreTextView.setText(approach.getScore() + "/10");
        if(approach.getFavourite() == 1) holder.isFavouriteButton.setImageResource(R.drawable.baseline_favorite_red_icon);
        else holder.isFavouriteButton.setImageResource(R.drawable.baseline_favorite_black_icon);
    }

    @Override
    public int getItemCount() {
        return approaches.size();
    }

    public static class ApproachHolder extends RecyclerView.ViewHolder {
        TextView leagueNameTextView, appraochDateTextView, approachScoreTextView;
        ImageButton isFavouriteButton, deleteButton;
        public ApproachHolder(@NonNull View itemView, HistoryRecycleViewInterface recyclerViewInterface) {
            super(itemView);
            leagueNameTextView = itemView.findViewById(R.id.league_name);
            appraochDateTextView = itemView.findViewById(R.id.approach_date);
            approachScoreTextView = itemView.findViewById(R.id.approach_score);
            isFavouriteButton = itemView.findViewById(R.id.isfavourite_imagebutton);
            deleteButton = itemView.findViewById(R.id.delete_imagebutton);

            isFavouriteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    recyclerViewInterface.onFavouriteButtonClick(isFavouriteButton, position);
                }
            });

            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    recyclerViewInterface.onDeleteButtonClick(deleteButton, position);
                }
            });
        }
    }
}

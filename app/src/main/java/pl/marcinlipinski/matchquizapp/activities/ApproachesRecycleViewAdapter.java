package pl.marcinlipinski.matchquizapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.marcinlipinski.matchquizapp.ApproachesRecycleViewInterface;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.Approach;
import java.util.ArrayList;

public class ApproachesRecycleViewAdapter extends RecyclerView.Adapter<ApproachesRecycleViewAdapter.ApproachHolder>{
    static ArrayList<Approach> approaches;
    ApproachesRecycleViewInterface recyclerViewInterface;
    Context context;

    public ApproachesRecycleViewAdapter(Context context, ArrayList<Approach> approaches, ApproachesRecycleViewInterface recyclerViewInterface){
        ApproachesRecycleViewAdapter.approaches = approaches;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }
    @NonNull
    @Override
    public ApproachesRecycleViewAdapter.ApproachHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.historyrecycleview_item, parent, false);
        return new ApproachesRecycleViewAdapter.ApproachHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproachesRecycleViewAdapter.ApproachHolder holder, int position) {
        Approach approach = approaches.get(position);
        holder.leagueNameTextView.setText(approach.getLeague());
        holder.appraochDateTextView.setText(approach.getApproachDate().toString());
        holder.approachScoreTextView.setText(approach.getScore() + "/10");
        if(approach.getFavourite() == 1) holder.isFavouriteButton.setImageResource(R.drawable.baseline_favorite_red);
        else holder.isFavouriteButton.setImageResource(R.drawable.baseline_favorite_black);
    }

    @Override
    public int getItemCount() {
        return approaches.size();
    }

    public static class ApproachHolder extends RecyclerView.ViewHolder {
        TextView leagueNameTextView, appraochDateTextView, approachScoreTextView;
        ImageButton isFavouriteButton, deleteButton;
        public ApproachHolder(@NonNull View itemView, ApproachesRecycleViewInterface recyclerViewInterface) {
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

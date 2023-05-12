package pl.marcinlipinski.matchquizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeasonsRecycleViewAdapter extends RecyclerView.Adapter<SeasonsRecycleViewAdapter.SeasonHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    static List<String> names;
    static List<Long> ids;

    public SeasonsRecycleViewAdapter(Context context, List<String> names, List<Long> ids, RecyclerViewInterface recyclerViewInterface){
        SeasonsRecycleViewAdapter.names = names;
        SeasonsRecycleViewAdapter.ids = ids;
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public SeasonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.season_item, parent, false);
        return new SeasonHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SeasonHolder holder, int position) {
        holder.seasonNameTextView.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class SeasonHolder extends RecyclerView.ViewHolder {
        TextView seasonNameTextView;
        public SeasonHolder(@NonNull @NotNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            seasonNameTextView = itemView.findViewById(R.id.season_name);
            itemView.setOnClickListener(view ->
            {
                if(recyclerViewInterface != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(ids.get(position));
                    }
                }
            });
        }


    }
}

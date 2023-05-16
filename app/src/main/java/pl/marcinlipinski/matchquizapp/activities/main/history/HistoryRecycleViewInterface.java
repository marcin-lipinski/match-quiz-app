package pl.marcinlipinski.matchquizapp.activities.main.history;

import android.widget.ImageButton;

public interface HistoryRecycleViewInterface {
    void onFavouriteButtonClick(ImageButton button, int approachPos);
    void onDeleteButtonClick(ImageButton button, int approachPos);
}

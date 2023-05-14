package pl.marcinlipinski.matchquizapp;

import android.widget.ImageButton;

public interface ApproachesRecycleViewInterface {
    void onFavouriteButtonClick(ImageButton button, int approachPos);
    void onDeleteButtonClick(ImageButton button, int approachPos);
}

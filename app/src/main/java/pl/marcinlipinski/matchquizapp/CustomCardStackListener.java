package pl.marcinlipinski.matchquizapp;

import android.widget.Button;
import com.yuyakaido.android.cardstackview.CardStackListener;

public interface CustomCardStackListener extends CardStackListener {
    void onButtonClick(Button button, Button[] buttons, int position);
}

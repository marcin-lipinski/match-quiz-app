package pl.marcinlipinski.matchquizapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.yuyakaido.android.cardstackview.*;
import pl.marcinlipinski.matchquizapp.CustomCardStackListener;
import pl.marcinlipinski.matchquizapp.QuestionQuizAdapter;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Event;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.VolleyCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static androidx.core.location.LocationManagerCompat.requestLocationUpdates;

public class QuizActivity extends Activity implements VolleyCallback<ArrayList<Event>>, LocationListener, CustomCardStackListener {
    EventService eventService;
    LocationManager locationManager;
    CardStackView cardStackView;
    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 5;
    private static final int GPS_DISTANCE = 1000;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;
    Location userLocation;
    private ArrayList<Event> chosenEvents;
    private final Random random = new Random();
    private int score;
    QuestionQuizAdapter adapter;
    CardStackLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        Long seasonId = getIntent().getLongExtra("seasonId", -1);

        provideLocation();

        SQLiteDatabaseContext databaseContext = new SQLiteDatabaseContext(QuizActivity.this);
        eventService = new EventService(databaseContext);
        eventService.getEventsBySeasonId(seasonId, this, QuizActivity.this);
    }

    @Override
    public void onSuccess(ArrayList<Event> result) {
        score = 0;
        chooseEventsToQuiz(result);

        manager = new CardStackLayoutManager(this, QuizActivity.this);
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(2);
        manager.setTranslationInterval(12.0f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setSwipeableMethod(SwipeableMethod.Automatic);

        adapter = new QuestionQuizAdapter(chosenEvents, this, this.eventService, QuizActivity.this, userLocation);
        cardStackView = findViewById(R.id.cardstackview);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private void chooseEventsToQuiz(ArrayList<Event> result) {
        Set<Event> tempChosenEvents = new HashSet<>();
        if(result.size()>= 10) while(tempChosenEvents.size() < 10) tempChosenEvents.add(result.get(random.nextInt(result.size())));
        else tempChosenEvents.addAll(result);

        chosenEvents = new ArrayList<>(tempChosenEvents);
    }

    @Override
    public void onFail(String message) {
        Log.d("Events by Id", message);
    }

    private void provideLocation() {
        requestPermissions(PERMISSIONS, PERMISSION_ALL);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestLocation();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        userLocation = location;
        locationManager.removeUpdates(this);
    }

    private void requestLocation() {
        if (locationManager == null) locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) requestLocation();
        else finish();
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.d("Swa", "Card swapped");
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {
        Random ran = new Random();
        int r = (ran.nextInt(256) << 16) & 0x00FF0000;
        int g = (ran.nextInt(256) << 8) & 0x0000FF00;
        int b = ran.nextInt(256) & 0x000000FF;
        view.setBackgroundColor(0xFF000000 | r | g | b);
        view.invalidate();
    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    @Override
    public void onButtonClick(Button button, Button[] buttons, int position) {
        for(Button but : buttons){
            if(button != but)but.setClickable(false);
        }

        String buttonText = String.valueOf(button.getText());
        Event event = chosenEvents.get(position);
        String correctString = event.getHomeTeamScore() + ":" + event.getAwayTeamScore();

        if(correctString.equals(buttonText)) {
            button.setBackgroundColor(0xFF00EE00);
            score++;
            Log.d("Poprawnie", correctString + " == " + buttonText + ", score = " + score);
        }
        else{
            button.setBackgroundColor(0xFFEE0000);
            Log.d("Niepoprawnie", correctString + " == " + buttonText + ", score = " + score);
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> cardStackView.swipe(), 2000);
        adapter.getCurrentCard().setClickable(true);
    }
}

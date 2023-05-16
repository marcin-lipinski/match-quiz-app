package pl.marcinlipinski.matchquizapp.activities.quiz;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.yuyakaido.android.cardstackview.*;
import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.dependecyInjection.AppInjector;
import pl.marcinlipinski.matchquizapp.models.Event;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.VolleyCallback;
import nl.dionsegijn.konfetti.xml.KonfettiView;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends Activity implements VolleyCallback<ArrayList<Event>>, LocationListener, CustomCardStackListener {
    @Inject
    EventService eventService;
    @Inject
    ApproachService approachService;
    LocationManager locationManager;
    CardStackView cardStackView;
    private KonfettiView konfettiView;
    private Shape.DrawableShape drawableShape;
    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 5;
    private static final int GPS_DISTANCE = 1000;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;
    Location userLocation;
    private ArrayList<Event> chosenEvents;
    private final Random random = new Random();
    private int score;
    QuestionsQuizAdapter adapter;
    CardStackLayoutManager manager;
    TextView questionNumberTextView, scoreLeagueName, yourScoreText, yourScoreValueText, scoreSeasonName;
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        AppInjector.inject(this);

        questionNumberTextView = findViewById(R.id.question_number);
        scoreLeagueName = findViewById(R.id.score_league_name);
        yourScoreText = findViewById(R.id.your_score);
        yourScoreValueText = findViewById(R.id.score_score);
        scoreSeasonName = findViewById(R.id.score_season_name);
        konfettiView = findViewById(R.id.konffettiView);
        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> onBackPressed());
        drawableShape = new Shape.DrawableShape(Objects.requireNonNull(ContextCompat.getDrawable(getApplicationContext(), R.drawable.baseline_favorite_black_icon)), true);

        Long seasonId = getIntent().getLongExtra("seasonId", -1);
        provideLocation();
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

        adapter = new QuestionsQuizAdapter(chosenEvents, this, this.eventService, QuizActivity.this, userLocation);
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
    public void onCardDragging(Direction direction, float ratio) {}

    @Override
    public void onCardSwiped(Direction direction) {}

    @Override
    public void onCardRewound() {}

    @Override
    public void onCardCanceled() {}

    @Override
    public void onCardAppeared(View view, int position) {
        String text = "Question " + (manager.getTopPosition() + 1) + " of " + adapter.getItemCount();
        questionNumberTextView.setText(text);
    }

    @Override
    public void onCardDisappeared(View view, int position) {}

    @Override
    public void onButtonClick(Button button, Button[] buttons, int position) {
        for(Button but : buttons) if(button != but)but.setClickable(false);

        String buttonText = String.valueOf(button.getText());
        Event event = chosenEvents.get(position);
        String correctString = event.getHomeTeamScore() + ":" + event.getAwayTeamScore();

        if(correctString.equals(buttonText)) {
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_green));
            score++;
        }
        else button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_button_red));

        cardStackView.postDelayed(() -> cardStackView.swipe(), 2000);
        if(position == adapter.getItemCount() - 1) showScoreBoard();
    }

    public void showScoreBoard(){
        String scoreText = "Points: " + score + "/" + adapter.getItemCount();
        String leagueName = "League: " + ApproachService.getTemporaryApproach().getLeague();
        String seasonName = "Season: " + ApproachService.getTemporaryApproach().getSeason();

        scoreLeagueName.setText(leagueName);
        scoreLeagueName.setText(seasonName);
        yourScoreValueText.setText(scoreText);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            yourScoreText.setVisibility(View.VISIBLE);
            yourScoreText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        }, 1800);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            scoreLeagueName.setVisibility(View.VISIBLE);
            scoreLeagueName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        }, 2100);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            scoreSeasonName.setVisibility(View.VISIBLE);
            scoreSeasonName.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        }, 2400);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            yourScoreValueText.setVisibility(View.VISIBLE);
            yourScoreValueText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            rain();
        }, 2700);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            returnButton.setVisibility(View.VISIBLE);
            returnButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        ApproachService.getTemporaryApproach().setScore(score);
        ApproachService.getTemporaryApproach().setApproachDate(LocalDate.now());
        approachService.saveGame();
        finish();
    }

    public void rain() {
        EmitterConfig emitterConfig = new Emitter(score * 1000L + 500, TimeUnit.MILLISECONDS).perSecond(100);
        konfettiView.start(
            new PartyFactory(emitterConfig)
                .angle(Angle.TOP)
                .spread(70)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(5f, 60f)
                .position(new Position.Relative(0.0, 1.0).between(new Position.Relative(1.0, 1.0)))
                .build()
        );
    }
}

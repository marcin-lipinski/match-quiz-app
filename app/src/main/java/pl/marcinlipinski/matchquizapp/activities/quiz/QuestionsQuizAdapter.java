package pl.marcinlipinski.matchquizapp.activities.quiz;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.Event;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.RetrofitCallback;

import java.text.DecimalFormat;
import java.util.*;

public class QuestionsQuizAdapter extends RecyclerView.Adapter<QuestionsQuizAdapter.CardHolder> {

    private final Random random;
    private final Location userLocation;
    CustomCardStackListener cardStackListener;
    ArrayList<Event> events;
    Context context;
    EventService eventService;
    DecimalFormat decimalFormat;
    Geocoder coder;

    public QuestionsQuizAdapter(ArrayList<Event> events, Context context, EventService eventService, CustomCardStackListener cardStackListener, Location userLocation) {
        this.events = events;
        this.context = context;
        this.eventService = eventService;
        this.userLocation = userLocation;
        this.cardStackListener = cardStackListener;
        this.random = new Random();
        this.decimalFormat = new DecimalFormat("#0.00");
        this.coder = new Geocoder(context);
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_question, parent, false);
        return new CardHolder(view);
    }

    public String findDistanceToCity(String city) {
        Address address;
        try {
            address = coder.getFromLocationName(city, 1).get(0);
            Location startPoint = new Location("locationA");
            startPoint.setLatitude(userLocation.getLatitude());
            startPoint.setLongitude(userLocation.getLongitude());

            Location endPoint = new Location("locationA");
            endPoint.setLatitude(address.getLatitude());
            endPoint.setLongitude(address.getLongitude());

            double distance = startPoint.distanceTo(endPoint) / 1000;
            return decimalFormat.format(distance) + " km";
        } catch (Exception e) {
            return "-----";
        }
    }

    public void findWinner(Long winnerId, CardHolder holder) {
        eventService.getCityDistanceByWinnerTeamId(winnerId, context, new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                holder.winnerCityDistanceTextView.setText(String.valueOf(findDistanceToCity(result)));
            }

            @Override
            public void onFail(String message) {
                Log.d("Winner City Distance", message);
            }
        });
    }

    private void generateOptions(CardHolder holder, Event event, int position) {
        int homeScore = event.getHomeTeamScore();
        int awayScore = event.getAwayTeamScore();
        int correctIndex = random.nextInt(4);
        Button[] buttons = new Button[]{holder.scoreOneButton, holder.scoreTwoButton, holder.scoreThreeButton, holder.scoreFourButton};
        List<String> scores = new ArrayList<>(Arrays.asList("-", "-", "-", "-"));
        String correctScore = homeScore + ":" + awayScore;

        scores.set(0, correctScore);
        if (homeScore == awayScore) {
            int goals = 0;
            for (int i = 1; i < 4; i++) {
                if(goals == homeScore) goals++;
                scores.set(i, goals + ":" + goals);
                goals++;
            }
        } else {
            for (int i = 1; i < 4; i++) {
                int scoreOne = random.nextInt(5);
                int scoreTwo = random.nextInt(5);
                String tempScore = scoreOne + ":" + scoreTwo;
                if (scores.contains(tempScore)) i--;
                else scores.set(i, tempScore);
            }
        }
        Collections.shuffle(scores, random);

        holder.scoreOneButton.setText(scores.get(0));
        holder.scoreTwoButton.setText(scores.get(1));
        holder.scoreThreeButton.setText(scores.get(2));
        holder.scoreFourButton.setText(scores.get(3));

        holder.scoreOneButton.setOnClickListener(view -> cardStackListener.onButtonClick((Button) view, buttons, position));
        holder.scoreTwoButton.setOnClickListener(view -> cardStackListener.onButtonClick((Button) view, buttons, position));
        holder.scoreThreeButton.setOnClickListener(view -> cardStackListener.onButtonClick((Button) view, buttons, position));
        holder.scoreFourButton.setOnClickListener(view -> cardStackListener.onButtonClick((Button) view, buttons, position));
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        Event event = events.get(position);
        generateOptions(holder, event, position);

        holder.winnerCityDistanceTextView.setText("-----");
        Picasso.with(context).load(event.getHomeTeamLogo()).fit().into(holder.homeTeamLogo);
        Picasso.with(context).load(event.getAwayTeamLogo()).fit().into(holder.awayTeamLogo);
        if (event.getWinnerCode() == 1) findWinner(event.getHomeTeamId(), holder);
        else if (event.getWinnerCode() == 2) findWinner(event.getAwayTeamId(), holder);

        holder.homeTeamTextView.setText(event.getHomeTeam());
        holder.awayTeamTextView.setText(event.getAwayTeam());
        holder.matchDateTextView.setText(event.getStartTime().toString());
        holder.scoreOneButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button_yellow));
        holder.scoreTwoButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button_yellow));
        holder.scoreThreeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button_yellow));
        holder.scoreFourButton.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button_yellow));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class CardHolder extends RecyclerView.ViewHolder {
        public TextView homeTeamTextView, awayTeamTextView, matchDateTextView, winnerCityDistanceTextView;
        public ImageView homeTeamLogo, awayTeamLogo;
        public Button scoreOneButton, scoreTwoButton, scoreThreeButton, scoreFourButton;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            homeTeamTextView = itemView.findViewById(R.id.home_team_name);
            awayTeamTextView = itemView.findViewById(R.id.away_team_name);
            matchDateTextView = itemView.findViewById(R.id.match_date);
            winnerCityDistanceTextView = itemView.findViewById(R.id.winner_city_distance);
            homeTeamLogo = itemView.findViewById(R.id.home_team_logo);
            awayTeamLogo = itemView.findViewById(R.id.away_team_logo);
            scoreOneButton = itemView.findViewById(R.id.score_one_button);
            scoreTwoButton = itemView.findViewById(R.id.score_two_button);
            scoreThreeButton = itemView.findViewById(R.id.score_three_button);
            scoreFourButton = itemView.findViewById(R.id.score_four_button);
        }
    }
}

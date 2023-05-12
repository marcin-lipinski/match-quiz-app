package pl.marcinlipinski.matchquizapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import pl.marcinlipinski.matchquizapp.activities.QuizActivity;
import pl.marcinlipinski.matchquizapp.models.Event;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.VolleyCallback;

import java.util.List;

public class QuestionQuizAdapter extends RecyclerView.Adapter<QuestionQuizAdapter.CardHolder> implements VolleyCallback<String>  {

    public QuestionQuizAdapter(List<Event> events, Context context, EventService eventService) {
        this.events = events;
        this.context = context;
        this.eventService = eventService;
    }

    List<Event> events;
    Context context;
    EventService eventService;
    @NonNull
    @NotNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.question_card, parent, false);
        return new CardHolder(view);
    }

    public String findDistanceToCity(String city){

        return "100";
    }

    public void findWinner(Long winnerId, CardHolder holder){
        eventService.getCityDistanceByWinnerTeamId(winnerId, context, new VolleyCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("city", result);
                holder.winnerCityDistanceTextView.setText(String.valueOf(findDistanceToCity(result)));
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull  CardHolder holder, int position) {
        Event event = events.get(position);

        holder.winnerCityDistanceTextView.setText("-----");
        if(event.getWinnerCode() == 1) findWinner(event.getHomeTeamId(), holder);
        else if(event.getWinnerCode() == 2) findWinner(event.getAwayTeamId(), holder);

        holder.homeTeamTextView.setText(event.getHomeTeam());
        holder.awayTeamTextView.setText(event.getAwayTeam());
        holder.matchDateTextView.setText(event.getStartTime().toString());
        holder.scoreOneButton.setText(event.getHomeTeam());
        holder.scoreTwoButton.setText(event.getHomeTeam());
        holder.scoreThreeButton.setText(event.getHomeTeam());
        Picasso.with(context).load(event.getHomeTeamLogo()).fit().into(holder.homeTeamLogo);
        Picasso.with(context).load(event.getAwayTeamLogo()).fit().into(holder.awayTeamLogo);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onFail() {

    }

    public static class CardHolder extends RecyclerView.ViewHolder {
        TextView homeTeamTextView, awayTeamTextView, matchDateTextView, winnerCityDistanceTextView;
        ImageView homeTeamLogo, awayTeamLogo;
        Button scoreOneButton, scoreTwoButton, scoreThreeButton;

        public CardHolder(@NonNull @NotNull View itemView) {
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
            itemView.setOnClickListener(view ->
            {

            });
        }


    }
}

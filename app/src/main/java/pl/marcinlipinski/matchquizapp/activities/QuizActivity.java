package pl.marcinlipinski.matchquizapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.yuyakaido.android.cardstackview.CardStackView;
import pl.marcinlipinski.matchquizapp.QuestionQuizAdapter;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.Event;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.VolleyCallback;

import java.util.ArrayList;

public class QuizActivity extends Activity implements VolleyCallback<ArrayList<Event>> {
    EventService eventService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        Long seasonId = getIntent().getLongExtra("seasonId", -1);

        SQLiteDatabaseContext databaseContext = new SQLiteDatabaseContext(QuizActivity.this);
        eventService = new EventService(databaseContext);

        ArrayList<Event> events = new ArrayList<>();
//        down d = new down(this, eventService, events, seasonId);
//        d.run();


        eventService.getEventsBySeasonId(seasonId, this, QuizActivity.this);
    }

    @Override
    public void onSuccess(ArrayList<Event> result) {
        QuestionQuizAdapter adapter = new QuestionQuizAdapter(result, this, this.eventService);
        CardStackView cardStackView = findViewById(R.id.cardstackview);
        cardStackView.setAdapter(adapter);
    }

    @Override
    public void onFail() {

    }

//    public class down extends Thread{
//        Semaphore s = new Semaphore(1);
//        Context context;
//        boolean lock = true;
//        int page = 1;
//        EventService eventService;
//        ArrayList<Event> events;
//        Long seasonId;
//        public down(Context context, EventService eventService, ArrayList<Event> events, Long seasonId){
//            this.context = context;
//            this.eventService = eventService;
//            this.events = events;
//            this.seasonId = seasonId;
//        }
//
//        @Override
//        public void run() {
//            while(page < 3)
//            {
//                if(lock)
//                {
//                    lock = false;
//                    Log.d("Proba", String.valueOf(page));
//                    eventService.getEventsBySeasonId(seasonId, context, events, page, new VolleyCallback<HashMap<Long, Event>>() {
//                        @Override
//                        public void onSuccess(HashMap<Long, Event> result) {
////                            for(Long key : result.keySet() ){
////                                Log.d(String.valueOf(key), result.get(key).getHomeTeam());
////                            }
//                            lock = true;
//                        }
//
//                        @Override
//                        public void onFail() {
//
//                        }
//                    });
//                    page++;
//                }
//            }
//        }
//    }
}

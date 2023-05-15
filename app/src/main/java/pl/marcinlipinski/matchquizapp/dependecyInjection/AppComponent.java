package pl.marcinlipinski.matchquizapp.dependecyInjection;

import dagger.Component;
import pl.marcinlipinski.matchquizapp.activities.*;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;

import javax.inject.Singleton;

@Singleton
@Component(modules = DatabaseModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(SeasonActivity activity);
    void inject(QuizActivity activity);
    LeaguesService provideLeagueService();
    HistoryFragment provideHistoryFragment();
    PlayFragment providePlayFragment();
    ApproachService provideApproachService();
    EventService provideEventService();
}

package pl.marcinlipinski.matchquizapp.dependecyInjection;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import pl.marcinlipinski.matchquizapp.activities.HistoryFragment;
import pl.marcinlipinski.matchquizapp.activities.PlayFragment;
import pl.marcinlipinski.matchquizapp.database.DatabaseContext;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;

import javax.inject.Singleton;

@Module
public class DatabaseModule {
    private final Context context;
    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    DatabaseContext provideDatabaseContext() {
        return new SQLiteDatabaseContext(context);
    }

    @Provides
    @Singleton
    LeaguesService provideLeagueService() {
        return new LeaguesService(provideDatabaseContext());
    }

    @Provides
    @Singleton
    ApproachService provideApproachService() {
        return new ApproachService(provideDatabaseContext());
    }

    @Provides
    @Singleton
    EventService provideEventService() {
        return new EventService(provideDatabaseContext());
    }

    @Provides
    @Singleton
    PlayFragment providePlayFragment() {
        return new PlayFragment(provideLeagueService());
    }

    @Provides
    @Singleton
    HistoryFragment provideHistoryFragment() {
        return new HistoryFragment(provideApproachService());
    }
}


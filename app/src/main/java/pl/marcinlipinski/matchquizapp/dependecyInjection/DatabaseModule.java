package pl.marcinlipinski.matchquizapp.dependecyInjection;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.activities.main.history.HistoryFragment;
import pl.marcinlipinski.matchquizapp.activities.main.play.PlayFragment;
import pl.marcinlipinski.matchquizapp.database.DatabaseContext;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.servicies.ApiService;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    LeaguesService provideLeagueService(DatabaseContext databaseContext, ApiService apiService) {
        return new LeaguesService(databaseContext, apiService);
    }

    @Provides
    @Singleton
    ApproachService provideApproachService(DatabaseContext databaseContext) {
        return new ApproachService(databaseContext);
    }

    @Provides
    @Singleton
    EventService provideEventService(DatabaseContext databaseContext, ApiService apiService) {
        return new EventService(databaseContext, apiService);
    }

    @Provides
    @Singleton
    PlayFragment providePlayFragment(LeaguesService leaguesService) {
        return new PlayFragment(leaguesService);
    }

    @Provides
    @Singleton
    HistoryFragment provideHistoryFragment(ApproachService approachService) {
        return new HistoryFragment(approachService);
    }

    @Provides
    @Singleton
    public ApiService provideMyApiService() {
        OkHttpClient httpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(ApiService.class);
    }
}


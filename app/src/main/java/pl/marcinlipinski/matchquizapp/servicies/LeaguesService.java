package pl.marcinlipinski.matchquizapp.servicies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.database.DatabaseContext;
import pl.marcinlipinski.matchquizapp.models.League;
import pl.marcinlipinski.matchquizapp.models.Season;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.ArrayList;

public class LeaguesService implements Service<League> {

    DatabaseContext databaseContext;
    ApiService apiService;

    @Inject
    public LeaguesService(DatabaseContext databaseContext, ApiService apiService) {
        this.databaseContext = databaseContext;
        this.apiService = apiService;
        this.initialize();
    }

    @Override
    public void initialize() {
        databaseContext.query("CREATE TABLE if not exists LEAGUE_TABLE (ID INTEGER PRIMARY KEY, NAME TEXT, LOGO TEXT)");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 191L + "', " + "'Ekstraklasa'" + ", 'https://tipsscore.com/resb/league/poland-ekstraklasa.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 191L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 251L + "', " + "'LaLiga'" + ", 'https://tipsscore.com/resb/league/spain-laliga.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 251L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 317L + "', " + "'Premier League'" + ", 'https://tipsscore.com/resb/league/england-premier-league.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 317L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 498L + "', " + "'Ligue 1'" + ", 'https://tipsscore.com/resb/league/france-ligue-1.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 498L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 512L + "', " + "'Bundesliga'" + ", 'https://tipsscore.com/resb/league/germany-bundesliga.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 512L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 592L + "', " + "'Serie A'" + ", 'https://tipsscore.com/resb/league/italy-serie-a.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 592L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 817L + "', " + "'UEFA Champions League'" + ", 'https://tipsscore.com/resb/league/europe-uefa-champions-league.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 817L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 818L + "', " + "'UEFA Europa League'" + ", 'https://tipsscore.com/resb/league/europe-uefa-europa-league.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 818L + ");");
        databaseContext.query("INSERT INTO LEAGUE_TABLE (ID, NAME, LOGO) SELECT '" + 8911L + "', " + "'UEFA Europa Conference League'" + ", 'https://tipsscore.com/resb/league/europe-uefa-europa-conference-league.png' WHERE NOT EXISTS (SELECT 1 FROM LEAGUE_TABLE WHERE ID = " + 8911L + ");");
    }

    public ArrayList<League> getAll() {
        SQLiteDatabase db = databaseContext.read();
        Cursor cursor = db.rawQuery("SELECT * FROM LEAGUE_TABLE", null);
        ArrayList<League> leagues = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                leagues.add(new League(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return leagues;
    }

    public void getSeasonsByLeagueId(Long leagueId, Context context, final RetrofitCallback<ArrayList<Season>> volleyCallback) {
        String apiKey = context.getResources().getString(R.string.api_key);
        String apiHost = context.getResources().getString(R.string.api_host_key);
        apiService.getSeasonsByLeagueId(leagueId, apiKey, apiHost).enqueue(new Callback<ApiResponse<ArrayList<Season>>>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse<ArrayList<Season>>> call, @NotNull Response<ApiResponse<ArrayList<Season>>> response) {
                if (!response.isSuccessful()) volleyCallback.onFail(response.message());
                try {
                    assert response.body() != null;
                    volleyCallback.onSuccess(response.body().data);
                } catch (Exception e) {
                    volleyCallback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse<ArrayList<Season>>> call, @NotNull Throwable throwable) {
                volleyCallback.onFail(throwable.getMessage());
            }
        });
    }

    @Override
    public ContentValues newContent(League league) {
        ContentValues cv = new ContentValues();
        cv.put("ID", league.getId());
        cv.put("NAME", league.getName());
        cv.put("LOGO", league.getLogo());
        return cv;
    }
}

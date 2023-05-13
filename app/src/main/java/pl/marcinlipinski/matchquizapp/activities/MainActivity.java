package pl.marcinlipinski.matchquizapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.jetbrains.annotations.NotNull;
import pl.marcinlipinski.matchquizapp.LeagueGridViewAdapter;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.database.SQLiteDatabaseContext;
import pl.marcinlipinski.matchquizapp.models.League;
import pl.marcinlipinski.matchquizapp.servicies.ApproachService;
import pl.marcinlipinski.matchquizapp.servicies.EventService;
import pl.marcinlipinski.matchquizapp.servicies.LeaguesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabaseContext databaseContext = new SQLiteDatabaseContext(MainActivity.this);
        LeaguesService leaguesService = new LeaguesService(databaseContext);
        leaguesService.initialize();
        EventService eventService = new EventService(databaseContext);
        ApproachService approachService = new ApproachService(databaseContext);
        eventService.initialize();


        ArrayList<League> leagues = leaguesService.getAll();

        GridView gridView;
        gridView = findViewById(R.id.leaguesGridView);
        gridView.setAdapter(new LeagueGridViewAdapter(this, leagues));

        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent myIntent = new Intent(this, ChoosingSeasonActivity.class);
            myIntent.putExtra("leagueName", leagues.get(i).getName());
            myIntent.putExtra("leagueId", leagues.get(i).getId());
            startActivity(myIntent);
        });
    }
}
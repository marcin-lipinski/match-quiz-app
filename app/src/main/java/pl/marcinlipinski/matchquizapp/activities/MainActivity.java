package pl.marcinlipinski.matchquizapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import pl.marcinlipinski.matchquizapp.*;
import pl.marcinlipinski.matchquizapp.dependecyInjection.AppInjector;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity{
    @Inject
    PlayFragment playFragment;
    @Inject
    HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);

        AppInjector.init(getApplicationContext());
        AppInjector.inject(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, playFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.play) getSupportFragmentManager().beginTransaction().replace(R.id.container, playFragment).commit();
            else getSupportFragmentManager().beginTransaction().replace(R.id.container, historyFragment).commit();
            return true;
        });
    }
}
package pl.marcinlipinski.matchquizapp.dependecyInjection;


import android.content.Context;
import pl.marcinlipinski.matchquizapp.activities.main.MainActivity;
import pl.marcinlipinski.matchquizapp.activities.quiz.QuizActivity;
import pl.marcinlipinski.matchquizapp.activities.seasons.SeasonActivity;

public class AppInjector {
    private static AppComponent appComponent;

    public static void init(Context context) {
        appComponent = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(context))
                .build();
    }

    public static void inject(MainActivity activity) {
        appComponent.inject(activity);
    }

    public static void inject(SeasonActivity activity) {
        appComponent.inject(activity);
    }

    public static void inject(QuizActivity activity) {
        appComponent.inject(activity);
    }
}

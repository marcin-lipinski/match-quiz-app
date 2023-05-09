package pl.marcinlipinski.matchquizapp;

import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TextView view = (TextView) findViewById(R.id.text2);
//
//        League l1 = new League(191, "Ekstraklasa", "https://tipsscore.com/resb/league/poland-ekstraklasa.png");
//        League l2 = new League(251,"LaLiga","https://tipsscore.com/resb/league/spain-laliga.png");
//
//        String url = "https://sportscore1.p.rapidapi.com/sports/1/leagues";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
//                response -> {
//                    // response
//                    Log.d("Response", response.toString());
//                    String r = "";
//                    try {
//                        JSONArray j = response.getJSONArray("data");//.getJSONObject(0).getJSONArray("data");
//                        Log.d("data", j.toString());
//                        for(int i = 0 ; i < j.length(); i++){
//                            Log.d(String.valueOf(i), j.getJSONObject(i).getString("slug"));
//                            r += j.getJSONObject(i).getString("slug") + "\n";
//                        }
//                    } catch (JSONException e) {
//                        Log.d("ERROR", e.getMessage());
//                        throw new RuntimeException(e);
//                    }
//                    view.setText(r);
//                },
//                error -> {
//                    // TODO Auto-generated method stub
//                    view.setText(error.getMessage());
//                    Log.d("ERROR","error => "+error.toString());
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("X-RapidAPI-Key", "bef5e777cemsh913f1631d392ffcp18bd9ejsn8198ee1dbb09");
//                params.put("X-RapidAPI-Host", "sportscore1.p.rapidapi.com");
//
//                return params;
//            }
//        };
//
//        //RequestQueue queue = Volley.newRequestQueue(this);
//        //queue.add(jsonObjectRequest);
//
        LeagueService leagueService = new LeagueService(MainActivity.this);
        leagueService.initializeDatabase();

        TextView view = (TextView) findViewById(R.id.text2);
        final String[] r = {""};
        leagueService.getAll().forEach(m -> r[0] += m.getName() + "\n");

        view.setText(r[0]);
    }
}
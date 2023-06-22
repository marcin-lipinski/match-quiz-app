package pl.marcinlipinski.matchquizapp.activities.main.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import pl.marcinlipinski.matchquizapp.R;
import pl.marcinlipinski.matchquizapp.models.League;

import java.util.ArrayList;

public class LeaguesGridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<League> leagues;
    LayoutInflater layoutInflater;

    public LeaguesGridViewAdapter(Context context, ArrayList<League> leagues) {
        this.context = context;
        this.leagues = leagues;
    }

    @Override
    public int getCount() {
        return leagues.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cardview_league, null);
        }

        ImageView imageView = view.findViewById(R.id.grid_image);
        TextView textView = view.findViewById(R.id.item_name);

        Picasso.with(context).load(leagues.get(i).getLogo()).fit().into(imageView);
        textView.setText(leagues.get(i).getName());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        return view;
    }
}

package edu.ncsu.walkwars.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.model.Team;

/**
 * Adapter for the ListActivity "LeaderboardActivity"
 * @author Tommy
 *
 */
public class LeaderboardListViewAdapter extends ArrayAdapter<Team>
{

    private Context context = null;
    private ArrayList<Team> teamList = null;

    /*
     * Constructor
     */
    public LeaderboardListViewAdapter(Context context, int textViewResourceId, List<Team> teamList)
    {
        super(context, textViewResourceId, teamList);
        this.context = context;
        this.teamList = (ArrayList<Team>) teamList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(edu.ncsu.walkwars.R.layout.list_item_layout_leaderboard, null);
        }
        Team team = teamList.get(position);
        
        
        TextView teamNameTextView = (TextView) v.findViewById(R.id.teamText);
        TextView scoreTextView = (TextView) v.findViewById(R.id.score);
        
        teamNameTextView.setText("Team: " + team.getTeamName());
        scoreTextView.setText(String.valueOf(team.getScore()));

        ImageView iv = (ImageView) v.findViewById(R.id.image);
        
        // Set the appropriate icon depending on the position: 1 for gold, 2 for silver, 3 for bronze, transparent image for everything else
        if(position == 0) {
            iv.setImageResource(R.drawable.icon_gold);
        }
        else if (position == 1) {
            iv.setImageResource(R.drawable.icon_silver);
        }
        else if (position == 2) {
            iv.setImageResource(R.drawable.icon_bronze);
        }
        else {
            //iv.setAlpha(0); // transparent
            iv.setImageResource(R.drawable.icon_center);
        }
        return v;
    }

}

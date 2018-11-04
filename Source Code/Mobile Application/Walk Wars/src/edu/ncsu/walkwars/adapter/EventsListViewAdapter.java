package edu.ncsu.walkwars.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.ncsu.walkwars.R;
import edu.ncsu.walkwars.model.Event;

/**
 * Adapter for the ListActivity "LeaderboardActivity"
 * @author Tommy
 *
 */
public class EventsListViewAdapter extends ArrayAdapter<Event>
{

    private Context context = null;
    private ArrayList<Event> eventList = null;

    /*
     * Constructor
     */
    public EventsListViewAdapter(Context context, int textViewResourceId, List<Event> eventList)
    {
        super(context, textViewResourceId, eventList);
        this.context = context;
        this.eventList = (ArrayList<Event>) eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(edu.ncsu.walkwars.R.layout.list_item_layout_events, null);
        }
        
        Event event = eventList.get(position);
        
        // Get the GUI elements
        TextView eventTextView = (TextView) v.findViewById(R.id.eventTextView);
        TextView bonusScoreTextView = (TextView) v.findViewById(R.id.bonusScoreTextView);
        TextView completedTextView = (TextView) v.findViewById(R.id.completedTextView);
        
        // 
        eventTextView.setText(event.getEvent());
        bonusScoreTextView.setText(String.valueOf(event.getPointValue()));
        
        
        if(event.isCompleted())
        {
            completedTextView.setText("Completed");
        }
        else
        {
            completedTextView.setText("Incomplete");
        }
        // ImageView iv = (ImageView) v.findViewById(R.id.image);

        return v;
    }

}

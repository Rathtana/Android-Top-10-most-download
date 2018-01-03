package com.example.top10downloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rathtana on 2/9/17.
 */

//Extending an ArrayAdapter and use it to create our own custom view
public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    //int resourse is the id of the list_record layout that will be used to display individual item
    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        //Takes an input from the xml file and build a view of object from it
        //context contains the thing about the application or activity that the system need in
        //order to manage it
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;

    }
    //Override the array Adapter class methods below
    //Returning the number of item in our list
    @Override
    public int getCount() {
        return applications.size();
    }

    /**
     * This method is automatically called when the list item view is ready to be displayed or about
     * to be displayed. The listView will call it when it want to display an item
     *
     * @param position position of the item within the adapter who view has been requested
     * @param convertView view of a row that left the screen. If it isn't null, ListView has
     *                    offered you a previous view. Having this previous view means you don't need to build a new row layout,
     *                    instead you must populate it with the correct data
     * @param parent is the view that will become the parent view that we're creating. Android callback automatically
     *               and will be the list_record XML in this case
     * @return
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //view holds the 3 view in our layoutResource
        //"Inflating" a view means taking the layout XML and parsing it to create the view and
        // ViewGroup objects from the elements
        //attach root is false because we don't want to attach our view to a root. The listView will
        //take care of doing that
        //parent is the list_record XML that will be use for this view
        View view = layoutInflater.inflate(layoutResource, parent, false );

        //Finding id that is part of this "view"
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvArtist = (TextView) view.findViewById(R.id.tvArtist);
        TextView tvSummary = (TextView) view.findViewById(R.id.tvSummary);

        //Getting the current FreeEntry object from the list
        FeedEntry currentApp = applications.get(position);

        //setting the display to the widgets
        tvName.setText(currentApp.getName());
        tvArtist.setText(currentApp.getArtist());
        tvSummary.setText(currentApp.getSummary());

        return view; //view holds the 3 TextView
    }

}

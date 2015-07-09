package xyz.praveen.iring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * A sample adapter to list items
 * <p/>
 * Created by praveen on 9/7/15.
 */
public class SampleListAdapter extends BaseAdapter {

    private final String SAMPLES[] = {"Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
            "Top New Free", "Trending"};
    private ArrayList<String> ITEMS = new ArrayList<String>();


    Context mContext;

    public SampleListAdapter(Context context) {
        mContext = context;
        addItemsToArray(randInt(30, 100));
    }

    @Override
    public int getCount() {
        return ITEMS.size();
    }

    @Override
    public Object getItem(int position) {
        return ITEMS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.listitem_sample,
                    parent, false);

            viewHolder.listitem = (TextView) convertView
                    .findViewById(R.id.item_text);

            // Save the holder with the view
            convertView.setTag(viewHolder);
        } else {
            // Just use the viewHolder and avoid findviewbyid()
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Assign value
        viewHolder.listitem.setText(ITEMS.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView listitem;
    }

    void addItemsToArray(int count) {
        for (int i = 0; i < count; i++) {
            ITEMS.add(SAMPLES[randInt(0, SAMPLES.length - 1)]);
        }

    }

    /**
     * Random number inclusive on Min and Max.
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}

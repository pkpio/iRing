package xyz.praveen.iring.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import xyz.praveen.iring.R;

/**
 * A sample adapter to show random cute images in a grid.
 * <p/>
 * Created by praveen on 9/7/15.
 */
public class SampleImageAdapter extends BaseAdapter {

    private Integer[] mAvailThumbIds = {
            R.drawable.ic_ing_1, R.drawable.ic_ing_2,
            R.drawable.ic_ing_3, R.drawable.ic_ing_4,
            R.drawable.ic_ing_5, R.drawable.ic_ing_6,
            R.drawable.ic_ing_7, R.drawable.ic_ing_8,
            R.drawable.ic_ing_9, R.drawable.ic_ing_10,
            R.drawable.ic_ing_11, R.drawable.ic_ing_12,
            R.drawable.ic_ing_13, R.drawable.ic_ing_14,
            R.drawable.ic_ing_15, R.drawable.ic_ing_16,
            R.drawable.ic_ing_17, R.drawable.ic_ing_18,
            R.drawable.ic_ing_19, R.drawable.ic_ing_20,
            R.drawable.ic_ing_21, R.drawable.ic_ing_22,
            R.drawable.ic_ing_23, R.drawable.ic_ing_24,
            R.drawable.ic_ing_25, R.drawable.ic_ing_26,
            R.drawable.ic_ing_27, R.drawable.ic_ing_28,
            R.drawable.ic_ing_29, R.drawable.ic_ing_30,
            R.drawable.ic_ing_31, R.drawable.ic_ing_32,
            R.drawable.ic_ing_33, R.drawable.ic_ing_34,
            R.drawable.ic_ing_35, R.drawable.ic_ing_36,
            R.drawable.ic_ing_37, R.drawable.ic_ing_38,
            R.drawable.ic_ing_39, R.drawable.ic_ing_40,
    };
    private ArrayList<Integer> mThumbIcons = new ArrayList<>();


    Context mContext;

    public SampleImageAdapter(Context context) {
        mContext = context;
        addItemsToArray(randInt(30, 100));
    }

    @Override
    public int getCount() {
        return mThumbIcons.size();
    }

    @Override
    public Object getItem(int position) {
        return mThumbIcons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(525, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIcons.get(position));
        return imageView;
    }

    void addItemsToArray(int count) {
        for (int i = 0; i < count; i++) {
            mThumbIcons.add(mAvailThumbIds[randInt(0, mAvailThumbIds.length - 1)]);
        }

    }

    /**
     * Random number inclusive on Min and Max.
     *
     * @param min Min
     * @param max Max
     * @return Random number
     */
    public static int randInt(int min, int max) {
        // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
        return new Random().nextInt((max - min) + 1) + min;
    }
}

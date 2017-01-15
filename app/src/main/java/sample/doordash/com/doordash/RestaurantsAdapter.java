package sample.doordash.com.doordash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Hakeem on 1/14/17.
 */

public class RestaurantsAdapter extends BaseAdapter{

    private List<Restaurant> mItems;
    private Context mContext;

    public RestaurantsAdapter(Context context, List<Restaurant> items){
        mItems = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void update(List<Restaurant> items){
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.restaurant_list_item,null);
            RestaurantViewHolder viewHolder = new RestaurantViewHolder(mContext, convertView, mItems.get(position));
            convertView.setTag(viewHolder);
        }else{
            RestaurantViewHolder viewHolder = (RestaurantViewHolder) convertView.getTag();
            viewHolder.update(mItems.get(position));
        }
        return convertView;
    }
}

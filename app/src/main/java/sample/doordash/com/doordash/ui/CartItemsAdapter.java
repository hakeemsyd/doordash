package sample.doordash.com.doordash.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItem;

/**
 * Created by Hakeem on 1/23/17.
 */

public class CartItemsAdapter extends BaseAdapter {
    private List<CartItem> mItems;
    private Context mContext;

    public CartItemsAdapter(Context context, List<CartItem> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.cart_item_view,null);
            CartItemViewHolder viewHolder = new CartItemViewHolder(mContext, convertView, mItems.get(position));
            convertView.setTag(viewHolder);
        }else{
            CartItemViewHolder viewHolder = (CartItemViewHolder) convertView.getTag();
            viewHolder.update(mItems.get(position));
        }
        return convertView;
    }

    public void update(List<CartItem> items){
        mItems.clear();
        mItems = items;
        notifyDataSetChanged();
    }
}

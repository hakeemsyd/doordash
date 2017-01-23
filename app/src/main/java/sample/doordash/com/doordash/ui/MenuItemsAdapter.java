package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sample.doordash.com.doordash.domain.MenuItem;

/**
 * Created by Hakeem on 1/22/17.
 */

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemViewHolder> {
    private List<MenuItem> mItems;
    private final Context mContext;
    private final View.OnClickListener mMenuItemClickListener;

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mMenuItemName;

        public MenuItemViewHolder(View view) {
            super(view);
            mMenuItemName = (TextView) view;
        }

        public void updateView(MenuItem item) {
            mMenuItemName.setText(item.mName);
        }

        public View getView(){
            return mMenuItemName;
        }
    }

    public MenuItemsAdapter(Context context, List<MenuItem> items, View.OnClickListener menuItemClickListener) {
        mContext = context;
        mItems = items;
        mMenuItemClickListener = menuItemClickListener;
    }

    public void update(List<MenuItem> items){
        this.mItems.clear();
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemViewHolder holder, int position) {
        holder.updateView(mItems.get(position));
        holder.getView().setTag(mItems.get(position));
        holder.getView().setOnClickListener(mMenuItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

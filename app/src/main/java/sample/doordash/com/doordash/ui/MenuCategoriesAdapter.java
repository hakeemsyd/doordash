package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.MenuCategory;

/**
 * Created by Hakeem on 1/22/17.
 */

public class MenuCategoriesAdapter extends RecyclerView.Adapter<MenuCategoriesAdapter.MenuCategoryViewHolder> {
    private List<MenuCategory> mItems;
    private Context mContext;

    public class MenuCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mCategoryName;

        public MenuCategoryViewHolder(View view) {
            super(view);
            mCategoryName = (TextView) view;
        }

        public void updateView(MenuCategory category) {
            mCategoryName.setText(category.mTitle);
        }
    }

    public MenuCategoriesAdapter(Context context, List<MenuCategory> items) {
        mContext = context;
        mItems = items;
    }

    public void update(List<MenuCategory> categories){
        this.mItems.clear();
        this.mItems = categories;
        notifyDataSetChanged();
    }

    @Override
    public MenuCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MenuCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuCategoryViewHolder holder, int position) {
        holder.updateView(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

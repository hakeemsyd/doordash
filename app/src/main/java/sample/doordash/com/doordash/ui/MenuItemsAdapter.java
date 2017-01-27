package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.MenuCategory;
import sample.doordash.com.doordash.domain.MenuItem;

/**
 * Created by Hakeem on 1/22/17.
 */

public class MenuItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> mItems;
    private final Context mContext;
    private final View.OnClickListener mMenuItemClickListener;

    public class MenuItemCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mCatName;
        private  TextView mSubtitle;
        private View mView;
        public MenuItemCategoryViewHolder(View view) {
            super(view);
            mView = view;
            mCatName = (TextView) view.findViewById(R.id.cat_title_name);
            mSubtitle = (TextView) view.findViewById(R.id.cat_subtitle);
        }

        public void updateView(MenuCategory cat) {
            mCatName.setText(cat.mTitle);
            mSubtitle.setText(cat.mSubtitle);
        }

        public View getView(){
            return mView;
        }
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mMenuItemName;
        private View mView;

        public MenuItemViewHolder(View view) {
            super(view);
            mView = view;
            mMenuItemName = (TextView) view.findViewById(R.id.menu_item_name);
        }

        public void updateView(MenuItem item) {
            mMenuItemName.setText(item.mName);
        }

        public View getView(){
            return mMenuItemName;
        }
    }

    public MenuItemsAdapter(Context context, List<Object> items, View.OnClickListener menuItemClickListener) {
        mContext = context;
        mItems = items;
        mMenuItemClickListener = menuItemClickListener;
    }

    public void update(List<Object> items){
        this.mItems.clear();
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position) instanceof  MenuCategory){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(viewType == 1){
            View view = inflater.inflate(R.layout.rec_menu_category, parent, false);
            return new MenuItemCategoryViewHolder(view);
        }else{
            View view = inflater.inflate(R.layout.rec_menu_item, parent, false);
            return new MenuItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mItems.get(position) instanceof  MenuCategory){
            MenuItemCategoryViewHolder vh = (MenuItemCategoryViewHolder) holder;
            vh.updateView((MenuCategory) mItems.get(position));
            //set click listener here
        }else if(mItems.get(position) instanceof MenuItem){
            MenuItemViewHolder vh = (MenuItemViewHolder) holder;
            vh.updateView((MenuItem)mItems.get(position));
            vh.getView().setTag(mItems.get(position));
            //holder.getView().setOnClickListener(mMenuItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

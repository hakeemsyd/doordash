package sample.doordash.com.doordash.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.Menu;
import sample.doordash.com.doordash.domain.MenuCategory;
import sample.doordash.com.doordash.domain.MenuItem;
import sample.doordash.com.doordash.service.DoorDashClient;

/**
 * Created by Hakeem on 1/21/17.
 */

public class MenuItemListFragment extends Fragment {

    private static final String KEY_RESTAURANT_ID = "restaurant_id";

    private RecyclerView mRecyclerView;
    private MenuItemsAdapter mAdapter;
    private Subscription mSubscription;

    public static MenuItemListFragment newInstance(long restaurantId) {
        MenuItemListFragment f = new MenuItemListFragment();
        Bundle b = new Bundle();
        b.putLong(KEY_RESTAURANT_ID, restaurantId);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_menu_categories, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_categories);
        mAdapter = new MenuItemsAdapter(getContext(), new ArrayList<MenuItem>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        long id = getArguments().getLong(KEY_RESTAURANT_ID, 0);
        if(id != 0){
            updateMenuItems(id);
        }else if(savedInstanceState != null){

        }
        return view;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void updateMenuItems(long restaurantId) {
            mSubscription = DoorDashClient.getInstance()
                    .getMenu(restaurantId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Menu>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mAdapter.update(new ArrayList<MenuItem>());
                        }

                        @Override
                        public void onNext(List<Menu> menu) {
                            List<MenuItem> items = new ArrayList<>();
                            for(Menu m : menu){
                                for(MenuCategory c : m.mMenuCategories){
                                    items.addAll(c.mItems);
                                }
                            }
                            mAdapter.update(items);
                        }
                    });
    }
}

package sample.doordash.com.doordash.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItem;
import sample.doordash.com.doordash.domain.MenuItem;
import sample.doordash.com.doordash.service.DoorDashClient;
import sample.doordash.com.doordash.storage.Storage;

/**
 * Created by Hakeem on 1/21/17.
 */

public class MenuItemDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String KEY_RESTAURANT_ID = "restaurant_id";
    private static final String KEY_ITEM_ID = "item_id";

    private ImageView mImage;
    private TextView mName;
    private TextView mDesc;
    private TextView mPrice;
    private Button mAddToCart;
    private MenuItem mItem;
    private Storage mStorage;
    private long mRestaurantId;
    private long mItemId;
    private List<Subscription> mSubscriptions = new ArrayList<>();

    public static final MenuItemDialogFragment newInstance(long restaurantId, long itemId) {
        MenuItemDialogFragment f = new MenuItemDialogFragment();
        Bundle b = new Bundle();
        b.putLong(KEY_RESTAURANT_ID, restaurantId);
        b.putLong(KEY_ITEM_ID, itemId);

        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_Alert);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_meu_item_deatials, container, false);
        mImage = (ImageView) view.findViewById(R.id.item_image);
        mName = (TextView) view.findViewById(R.id.item_name);
        mDesc = (TextView) view.findViewById(R.id.item_desc);
        mPrice = (TextView) view.findViewById(R.id.item_price);
        mAddToCart = (Button) view.findViewById(R.id.btn_add_cart);
        mAddToCart.setOnClickListener(this);

        mRestaurantId = getArguments().getLong(KEY_RESTAURANT_ID, 0);
        mItemId = getArguments().getLong(KEY_ITEM_ID, 0);

        if (mRestaurantId != 0 && mItemId != 0) {
            updateView(mRestaurantId, mItemId);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStorage = new Storage(getActivity());
    }

    @Override
    public void onDestroy() {
        for (Subscription sub : mSubscriptions) {
            if (!sub.isUnsubscribed()) {
                sub.unsubscribe();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Subscription sub = mStorage.addCartItem(new CartItem(-1, mRestaurantId, mItemId, 1, mItem))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Item could not be added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Toast.makeText(getActivity(), "Item added to cart: " + mItemId, Toast.LENGTH_SHORT).show();
                    }
                });

        mSubscriptions.add(sub);
    }

    private void updateView(long restaurantId, long itemId) {
        Subscription sub = DoorDashClient.getInstance()
                .getMenuItem(restaurantId, itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MenuItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MenuItem menuItem) {
                        mItem = menuItem;
                        mName.setText(menuItem.mName);
                        mDesc.setText(menuItem.mDesc);
                        mPrice.setText("Price: " + menuItem.mPrice + "");
                        getActivity().setTitle(menuItem.mName);
                        if(menuItem.mImageSrc != null && !menuItem.mImageSrc.isEmpty()){
                            Picasso.with(getActivity()).load(menuItem.mImageSrc).into(mImage);
                        }
                    }
                });

        mSubscriptions.add(sub);
    }
}

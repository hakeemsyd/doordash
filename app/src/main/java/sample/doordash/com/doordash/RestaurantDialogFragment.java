package sample.doordash.com.doordash;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hakeem on 1/15/17.
 */

public class RestaurantDialogFragment extends DialogFragment {

    private long mId;
    private TextView mRestName;
    private TextView mRestPhone;
    private TextView mAddress;
    private TextView mDistance;
    private ImageView mIcon;
    private Subscription mSubscription;

    static RestaurantDialogFragment newInstance(long id) {
        RestaurantDialogFragment f = new RestaurantDialogFragment();

        Bundle args = new Bundle();
        args.putLong(Constants.KEY_JSON_RESTAURANT_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getArguments().getLong(Constants.KEY_JSON_RESTAURANT_ID);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_Alert);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_restaurant_detail, container, false);
        mRestName = (TextView) v.findViewById(R.id.resinfo_name);
        mAddress = (TextView) v.findViewById(R.id.resinfo_address);
        mDistance = (TextView) v.findViewById(R.id.resinfo_distance);
        mRestPhone = (TextView) v.findViewById(R.id.resinfo_phone);
        mIcon = (ImageView) v.findViewById(R.id.restinfo_icon);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    public void update() {
        mSubscription = DoorDashClient.getInstance().getRestaurant(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Restaurant>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Failed to load restaurant", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Restaurant restaurant) {
                        mRestName.setText("Name: " + restaurant.mName);
                        mRestPhone.setText("Pone: " + restaurant.mPhone);
                        mAddress.setText("Address: " + restaurant.mAddress.mPrintableAddress);
                        mDistance.setText("Distance: " + restaurant.mDistance);
                        Picasso.with(getActivity()).load(restaurant.mCoverImageSrc).into(mIcon);

                    }
                });
    }
}

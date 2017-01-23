package sample.doordash.com.doordash.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.doordash.com.doordash.R;
import sample.doordash.com.doordash.domain.CartItem;
import sample.doordash.com.doordash.storage.Storage;

/**
 * Created by Hakeem on 1/23/17.
 */

public class CartItemViewHolder {
    private TextView mName;
    private Button mRemove;
    private Context mContext;
    private CartItem mItem;

    public CartItemViewHolder(Context context, View view, CartItem item){
        mContext = context;
        mItem = item;
        mName = (TextView) view.findViewById(R.id.cart_item_name);
        mRemove = (Button) view.findViewById(R.id.remove_item);
        update();
    }

    public void update(CartItem item){
        mItem = item;
        update();
    }

    private void update(){
        mName.setText("ItemId: "+ mItem.mId + " x " + mItem.mQuantity);
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage s = new Storage(mContext);
                s.removeCartItem(mItem)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Toast.makeText(mContext, "Item Removed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}

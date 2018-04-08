package com.parthiv.user.cart;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.user.Utils.Resource;
import com.parthiv.user.api.APIClient;
import com.parthiv.user.model.Item;
import com.parthiv.user.model.Order;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {

    private static final String TAG = "ItemViewModel";

    private LiveData<Resource<List<Item>>> mItemLiveData;

    private LiveData<Resource<JSONObject>> mOrderLiveData;

    public LiveData<Resource<List<Item>>> getItemLiveData(String ids) {

        Publisher<Resource<List<Item>>> publisher = APIClient.getCampusCartAPI()
                .getItemsByIds(ids)
                .map(Resource::success)
                .onErrorReturn(throwable -> {
                    Log.e(TAG,throwable.toString());
                    return Resource.error(throwable.getMessage(), null);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);

        mItemLiveData = LiveDataReactiveStreams.fromPublisher(publisher);

        return mItemLiveData;
    }

    public LiveData<Resource<JSONObject>> postOrder(Order order) {
        Publisher<Resource<JSONObject>> publisher = APIClient.getCampusCartAPI()
                .postOrder(order)
                .map(Resource::success)
                .onErrorReturn(throwable -> {
                    Log.e(TAG,throwable.toString());
                    return Resource.error(throwable.getMessage(), null);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);

        mOrderLiveData = LiveDataReactiveStreams.fromPublisher(publisher);

        return mOrderLiveData;
    }
}

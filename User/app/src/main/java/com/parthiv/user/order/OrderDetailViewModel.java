package com.parthiv.user.order;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.user.Utils.Resource;
import com.parthiv.user.api.APIClient;
import com.parthiv.user.model.Item;
import com.parthiv.user.model.Order;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailViewModel extends ViewModel {

    private static final String TAG = "OrderDetailViewModel";

    private LiveData<Resource<Order>> mOrderDetailLiveData;

    private LiveData<Resource<List<Item>>> mItemLiveData;

    public LiveData<Resource<Order>> getOrderLiveData(long id) {
        Publisher<Resource<Order>> publisher = APIClient.getCampusCartAPI()
                .getOrderByOrderId(id)
                .map(Resource::success)
                .onErrorReturn(throwable -> {
                    Log.e(TAG, throwable.toString());
                    return Resource.error(throwable.getMessage(), null);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);

        mOrderDetailLiveData = LiveDataReactiveStreams.fromPublisher(publisher);

        return mOrderDetailLiveData;
    }


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
}

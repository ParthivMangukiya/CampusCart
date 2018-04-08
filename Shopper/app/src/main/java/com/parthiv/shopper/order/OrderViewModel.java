package com.parthiv.shopper.order;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.api.APIClient;
import com.parthiv.shopper.model.Order;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderViewModel extends ViewModel {

    private static final String TAG = "OrderViewModel";

    private LiveData<Resource<List<Order>>> mOrderLiveData;

    public LiveData<Resource<List<Order>>> getOrderLiveData() {
        Publisher<Resource<List<Order>>> publisher = APIClient.getCampusCartAPI()
                .getOrders()
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

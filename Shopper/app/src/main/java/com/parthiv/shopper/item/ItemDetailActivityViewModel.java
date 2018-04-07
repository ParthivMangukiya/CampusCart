package com.parthiv.shopper.item;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.api.APIClient;
import com.parthiv.shopper.model.Item;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ItemDetailActivityViewModel extends ViewModel {

    private static final String TAG = "ItemViewModel";

    private LiveData<Resource<Item>> mItemLiveData;

    public LiveData<Resource<Item>> getItemLiveData(long id) {
        Publisher<Resource<Item>> publisher = APIClient.getCampusCartAPI()
                .getItemById(id)
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


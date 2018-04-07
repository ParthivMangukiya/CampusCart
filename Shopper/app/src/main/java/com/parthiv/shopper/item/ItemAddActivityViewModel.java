package com.parthiv.shopper.item;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.api.APIClient;
import com.parthiv.shopper.model.Item;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ItemAddActivityViewModel extends ViewModel {

    private static final String TAG = "ItemAddViewModel";

    private LiveData<Resource<JSONObject>> mItemLiveData;

    public LiveData<Resource<JSONObject>> postItemLiveData(Item item) {
        Publisher<Resource<JSONObject>> publisher = APIClient.getCampusCartAPI()
                .postItem(item)
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


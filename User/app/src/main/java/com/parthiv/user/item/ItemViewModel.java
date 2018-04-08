package com.parthiv.user.item;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.user.Utils.Resource;
import com.parthiv.user.api.APIClient;
import com.parthiv.user.model.Item;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ItemViewModel extends ViewModel {

    private static final String TAG = "ItemViewModel";

    private LiveData<Resource<List<Item>>> mItemLiveData;

    public LiveData<Resource<List<Item>>> getItemLiveData() {
        Publisher<Resource<List<Item>>> publisher = APIClient.getCampusCartAPI()
                .getItems()
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

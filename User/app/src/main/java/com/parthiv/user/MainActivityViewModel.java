package com.parthiv.user;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.parthiv.user.Utils.Resource;
import com.parthiv.user.api.APIClient;
import com.parthiv.user.model.User;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "ItemViewModel";

    private LiveData<Resource<User>> mUserLiveData;

    public LiveData<Resource<User>> postUser(User user) {
        Publisher<Resource<User>> publisher = APIClient.getCampusCartAPI()
                .postUser(user)
                .map(Resource::success)
                .onErrorReturn(throwable -> {
                    Log.e(TAG,throwable.toString());
                    return Resource.error(throwable.getMessage(), null);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);

        mUserLiveData = LiveDataReactiveStreams.fromPublisher(publisher);

        return mUserLiveData;
    }
}


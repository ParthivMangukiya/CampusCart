package com.parthiv.shopper.item;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parthiv.shopper.R;
import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.model.Item;

import java.util.ArrayList;

public class ItemFragment extends Fragment {

    RecyclerView itemRecycler;
    ItemAdapter mItemAdapter;
    ItemViewModel mItemViewModel;
    private TextView emptyView;

    public ItemFragment() {
        // Required empty public constructor
    }

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        mItemAdapter = new ItemAdapter(new ArrayList<Item>());
        emptyView = rootView.findViewById(R.id.txt_no_items);
        itemRecycler = rootView.findViewById(R.id.item_recycler_view);
        itemRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemRecycler.setItemAnimator(new DefaultItemAnimator());
        itemRecycler.setAdapter(mItemAdapter);
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        mItemViewModel.getItemLiveData().observe(this,listResource -> {
            if(listResource.status == Resource.Status.SUCCESS){
                mItemAdapter.updateList(listResource.data);
                emptyView.setVisibility(View.GONE);
            }else{
                emptyView.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

}

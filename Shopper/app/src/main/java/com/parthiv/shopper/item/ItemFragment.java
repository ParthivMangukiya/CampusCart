package com.parthiv.shopper.item;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class ItemFragment extends Fragment implements ItemAdapter.ItemClickListener {

    private RecyclerView itemRecycler;
    private ItemAdapter mItemAdapter;
    private ItemViewModel mItemViewModel;
    private TextView emptyView;
    private FloatingActionButton addButton;

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
        mItemAdapter = new ItemAdapter(new ArrayList<Item>(),this);
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
        addButton = rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener((v)->onFabClick());
        return rootView;
    }

    private void onFabClick() {
        Intent intent = new Intent(getActivity(), ItemAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(long id) {
        Intent intent = new Intent(getActivity(),ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.ITEM_DETAIL_URI,id);
        startActivity(intent);
    }
}

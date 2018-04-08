package com.parthiv.user.item;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parthiv.user.R;
import com.parthiv.user.Utils.Resource;
import com.parthiv.user.model.Item;

import java.util.ArrayList;

public class ItemFragment extends Fragment implements ItemAdapter.ItemClickListener {

    private RecyclerView itemRecycler;
    private ItemAdapter mItemAdapter;
    private ItemViewModel mItemViewModel;
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
        return rootView;
    }

    @Override
    public void onItemClick(long id) {
        Intent intent = new Intent(getActivity(),ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.ITEM_DETAIL_URI,id);
        startActivity(intent);
    }
}

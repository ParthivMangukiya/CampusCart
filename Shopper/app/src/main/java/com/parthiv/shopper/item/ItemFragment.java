package com.parthiv.shopper.item;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                onAddClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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



    private void onAddClick() {
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

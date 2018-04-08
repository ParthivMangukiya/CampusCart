package com.parthiv.shopper.order;

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

import com.parthiv.shopper.R;
import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.item.ItemAdapter;
import com.parthiv.shopper.model.Order;

import java.util.ArrayList;

public class OrderListFragment extends Fragment implements ItemAdapter.ItemClickListener {

    private RecyclerView orderRecycler;
    private OrderAdapter mOrderAdapter;
    private OrderViewModel mOrderViewModel;
    private TextView emptyView;

    public OrderListFragment() {
        // Required empty public constructor
    }

    public static OrderListFragment newInstance() {
        return new OrderListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        mOrderAdapter = new OrderAdapter(new ArrayList<Order>(), this, getActivity());
        emptyView = rootView.findViewById(R.id.txt_no_orders);
        orderRecycler = rootView.findViewById(R.id.order_recycler_view);
        orderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderRecycler.setItemAnimator(new DefaultItemAnimator());
        orderRecycler.setAdapter(mOrderAdapter);
        mOrderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        mOrderViewModel.getOrderLiveData().observe(this, listResource -> {
            if (listResource.status == Resource.Status.SUCCESS) {
                mOrderAdapter.updateList(listResource.data);
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    @Override
    public void onItemClick(long id) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.ORDER_DETAIL_URI, id);
        startActivity(intent);
    }
}


package com.parthiv.user.order;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parthiv.user.R;
import com.parthiv.user.Utils.Resource;
import com.parthiv.user.Utils.SharedPreferenceUtils;
import com.parthiv.user.item.ItemAdapter;
import com.parthiv.user.model.Order;

import java.util.ArrayList;

public class OrderListFragment extends Fragment implements ItemAdapter.ItemClickListener {

    private RecyclerView orderRecycler;
    private OrderAdapter mOrderAdapter;
    private OrderViewModel mOrderViewModel;
    private TextView emptyView;
    private static final String TAG = "OrderListFragment";

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
        long uid = SharedPreferenceUtils.getLongPreference(getActivity(),"uid",0);
        mOrderViewModel.getOrderLiveData(uid).observe(this, listResource -> {
            if (listResource.status == Resource.Status.SUCCESS) {
                mOrderAdapter.updateList(listResource.data);
                Log.d(TAG,listResource.data.get(0).toString());
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


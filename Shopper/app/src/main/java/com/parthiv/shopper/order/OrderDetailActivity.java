package com.parthiv.shopper.order;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parthiv.shopper.R;
import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.item.ItemAdapter;
import com.parthiv.shopper.item.ItemDetailActivity;
import com.parthiv.shopper.model.Item;
import com.parthiv.shopper.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements ItemAdapter.ItemClickListener {

    public static final String ORDER_DETAIL_URI = "order_detail_uri";
    private RecyclerView itemRecycler;
    private ItemAdapter mItemAdapter;
    private OrderDetailViewModel mOrderDetailViewModel;
    private TextView emptyView;
    private TextView priceTextView;
    private TextView otpTextView;
    private Button packBtn;
    private Order order;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        long id = getIntent().getLongExtra(ORDER_DETAIL_URI,0);
        mItemAdapter = new ItemAdapter(new ArrayList<Item>(),this);
        emptyView = findViewById(R.id.txt_no_items);
        itemRecycler = findViewById(R.id.item_recycler_view);
        itemRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemRecycler.setItemAnimator(new DefaultItemAnimator());
        itemRecycler.setAdapter(mItemAdapter);
        packBtn = findViewById(R.id.packBtn);
        priceTextView = findViewById(R.id.priceTextView);
        otpTextView = findViewById(R.id.otpTextView);
        mOrderDetailViewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        packBtn.setVisibility(View.GONE);
        packBtn.setOnClickListener((v) -> {
            order.setStatus("packed");
            mOrderDetailViewModel.patchOrder(order).observe(this, jsonObjectResource -> {
                if(jsonObjectResource!= null && jsonObjectResource.status == Resource.Status.SUCCESS){
                    Toast.makeText(this,"Order Packed",Toast.LENGTH_LONG).show();
                    mOrderDetailViewModel.mOrderLiveData.removeObservers(this);
                    packBtn.setText("Packed");
                }else{
                    mOrderDetailViewModel.mOrderLiveData.removeObservers(this);
                    Toast.makeText(this,"Order Not Packed",Toast.LENGTH_LONG).show();
                    packBtn.setText("Pack");
                }
            });
        });
        mOrderDetailViewModel.getOrderLiveData(id).observe(this,orderResource -> {
            if(orderResource.status == Resource.Status.SUCCESS){
                order = orderResource.data;
                priceTextView.setText(String.format("₹ %.2f",order.getAmount()));
                List<Long> ids = order.getItems();
                StringBuilder str = new StringBuilder();
                for(Long iid : ids){
                    str.append(iid).append(",");
                }
                if(order.getStatus().equals("pending")){
                    packBtn.setText("Pack");
                }else{
                    packBtn.setText("Packed");
                }
                otpTextView.setText(order.getOtp() + "");
                mOrderDetailViewModel.getItemLiveData(str.toString().substring(0,str.length()-1)).observe(this,listResource -> {
                    if(listResource.status == Resource.Status.SUCCESS){
                        mItemAdapter.updateList(listResource.data);
                        packBtn.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }else{
                        emptyView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    @Override
    public void onItemClick(long id) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.ITEM_DETAIL_URI,id);
        startActivity(intent);
    }
}

package com.parthiv.shopper.order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.parthiv.shopper.R;
import com.parthiv.shopper.item.ItemAdapter;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String ORDER_DETAIL_URI = "order_detail_uri";
    private ItemAdapter mItemAdapter;
    private OrderDetailViewModel mOrderDetailViewModel;
    private TextView emptyView;
    private TextView priceTextView;
    private Button purchaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        long id = getIntent().getLongExtra(ORDER_DETAIL_URI,0);

    }

}

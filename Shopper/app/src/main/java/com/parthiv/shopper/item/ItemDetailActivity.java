package com.parthiv.shopper.item;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.parthiv.shopper.R;
import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.model.Item;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String ITEM_DETAIL_URI = "item_detail_id_uri";

    TextView nameTextView;
    TextView descriptionTextView;
    TextView categoryTextView;
    ImageView itemImageView;
    TextView priceTextView;
    ItemDetailActivityViewModel mItemViewModel;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        long id = getIntent().getLongExtra(ITEM_DETAIL_URI,0);
        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.description);
        categoryTextView = findViewById(R.id.status);
        itemImageView = findViewById(R.id.itemImageView);
        priceTextView = findViewById(R.id.amount);
        mItemViewModel = ViewModelProviders.of(this).get(ItemDetailActivityViewModel.class);
        mItemViewModel.getItemLiveData(id).observe(this,itemResource -> {
            if(itemResource.status == Resource.Status.SUCCESS){
                Item item = itemResource.data;
                nameTextView.setText(item.getName());
                descriptionTextView.setText(item.getDescription());
                categoryTextView.setText(item.getCategory());
                priceTextView.setText(String.format("₹ %.2f",item.getPrice()));
                if(item.getImageUri()!= null)
                    Picasso.with(this)
                            .load(Uri.parse(item.getImageUri()))
                            .error(R.drawable.ic_local_grocery_store_black_24dp)
                            .into(itemImageView);
            }
        });
    }

}

package com.parthiv.user.item;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parthiv.user.R;
import com.parthiv.user.Utils.Resource;
import com.parthiv.user.Utils.SharedPreferenceUtils;
import com.parthiv.user.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String ITEM_DETAIL_URI = "item_detail_id_uri";

    TextView nameTextView;
    TextView descriptionTextView;
    TextView categoryTextView;
    TextView priceTextView;
    ImageView itemImageView;
    Button addToCartButton;
    Item item;
    ItemDetailActivityViewModel mItemViewModel;
    List<Long> ids;
    private static final String TAG = "ItemDetailActivity";
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        long id = getIntent().getLongExtra(ITEM_DETAIL_URI,0);
        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.description);
        categoryTextView = findViewById(R.id.category);
        addToCartButton = findViewById(R.id.addToCart);
        itemImageView = findViewById(R.id.itemImageView);
        priceTextView = findViewById(R.id.price);
        ids = new ArrayList<>();
        String savedString = SharedPreferenceUtils.getStringPreference(this,"ids");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        Log.d(TAG,savedString.length() + "");
        ids = new ArrayList<>();
        while(st.hasMoreTokens()){
            ids.add(Long.parseLong(st.nextToken()));
        }
        mItemViewModel = ViewModelProviders.of(this).get(ItemDetailActivityViewModel.class);
        mItemViewModel.getItemLiveData(id).observe(this,itemResource -> {
            if(itemResource.status == Resource.Status.SUCCESS){
                item = itemResource.data;
                nameTextView.setText(item.getName());
                descriptionTextView.setText(item.getDescription());
                categoryTextView.setText(item.getCategory());
                priceTextView.setText(String.format("₹ %.2f",item.getPrice()));
                if(item.getImageUri()!= null)
                    Picasso.with(this)
                            .load(Uri.parse(item.getImageUri()))
                            .into(itemImageView);
                if(ids.contains(item.getId())){
                    addToCartButton.setText("Remove From Cart");
                }else{
                    addToCartButton.setText("Add to Cart");
                }
            }
        });
        addToCartButton.setOnClickListener((v) -> {
            Log.d(TAG,"added");
            if(item != null){
                if(!ids.contains(item.getId())){
                    ids.add(item.getId());
                }else{
                    ids.remove(item.getId());
                }
            }
            Log.d(TAG, String.valueOf(ids));
            StringBuilder str = new StringBuilder();
            for(Long iid : ids){
                str.append(iid).append(",");
            }
            String s = str.toString();
            if(str.length()-1>0)
                SharedPreferenceUtils.setStringPreference(this,"ids",s.substring(0,str.length()-1));
            else
                SharedPreferenceUtils.setStringPreference(this,"ids","");

        });
    }

}

package com.parthiv.user.cart;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parthiv.user.R;
import com.parthiv.user.Utils.Resource;
import com.parthiv.user.Utils.SharedPreferenceUtils;
import com.parthiv.user.item.ItemAdapter;
import com.parthiv.user.item.ItemDetailActivity;
import com.parthiv.user.model.Item;
import com.parthiv.user.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class CartFragment extends Fragment implements ItemAdapter.ItemClickListener,AdapterView.OnItemSelectedListener {

    private RecyclerView itemRecycler;
    private ItemAdapter mItemAdapter;
    private CartViewModel mCartViewModel;
    private TextView emptyView;
    private TextView priceTextView;
    private Button purchaseBtn;
    private double total;
    private int color;
    TextView textView;
    Spinner mSpinner;
    private static final String TAG = "CartFragment";

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        mItemAdapter = new ItemAdapter(new ArrayList<Item>(),this);
        emptyView = rootView.findViewById(R.id.txt_no_items);
        mSpinner = rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textView = rootView.findViewById(R.id.textView15);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
        itemRecycler = rootView.findViewById(R.id.item_recycler_view);
        itemRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemRecycler.setItemAnimator(new DefaultItemAnimator());
        itemRecycler.setAdapter(mItemAdapter);
        purchaseBtn = rootView.findViewById(R.id.purchaseBtn);
        priceTextView = rootView.findViewById(R.id.priceTextView);
        mCartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        purchaseBtn.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        priceTextView.setVisibility(View.GONE);
        mSpinner.setVisibility(View.GONE);
        purchaseBtn.setOnClickListener((v) -> {
            Random rnd = new Random();
            int otp = rnd.nextInt(999999 - 111111 + 1) + 111111;
            String savedString = SharedPreferenceUtils.getStringPreference(getActivity(),"ids");
            StringTokenizer st = new StringTokenizer(savedString, ",");
            List<Long> ids = new ArrayList<>();
            while(st.hasMoreTokens()){
                ids.add(Long.parseLong(st.nextToken()));
            }
            long uid = SharedPreferenceUtils.getLongPreference(getActivity(),"uid",0);
            //Todo: add uid
            Order order = new Order(0,uid,"",otp,"pending",total,color,ids);

            mCartViewModel.postOrder(order).observe(this, jsonObjectResource -> {
                if(jsonObjectResource!= null && jsonObjectResource.status == Resource.Status.SUCCESS){
                    Toast.makeText(getActivity(),"Order Placed",Toast.LENGTH_LONG).show();
                    SharedPreferenceUtils.setStringPreference(getActivity(),"ids","");
                    mItemAdapter.updateList(new ArrayList<>());
                }else{
                    Toast.makeText(getActivity(),"Order Not Placed",Toast.LENGTH_LONG).show();
                }
            });
        });
        refresh();

        return rootView;
    }

    public void refresh(){
        String savedString = SharedPreferenceUtils.getStringPreference(getActivity(),"ids");
        Log.d(TAG,savedString);
        if(savedString.length() > 0){
            mCartViewModel.getItemLiveData(savedString).observe(this,listResource -> {
                if(listResource.status == Resource.Status.SUCCESS){
                    List<Item> ls = listResource.data;
                    total = 0;
                    if(ls != null){
                        for (Item item: ls) {
                            total += item.getPrice();
                        }
                    }
                    mItemAdapter.updateList(ls);
                    priceTextView.setText(String.format("₹ %.2f",total));
                    purchaseBtn.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    priceTextView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    mSpinner.setVisibility(View.VISIBLE);
                }else{
                    priceTextView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    purchaseBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mSpinner.setVisibility(View.GONE);
                }
            });
        }
    }



    @SuppressLint("DefaultLocale")
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onItemClick(long id) {
        Intent intent = new Intent(getActivity(),ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.ITEM_DETAIL_URI,id);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        color = position + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        color = 0;
    }
}

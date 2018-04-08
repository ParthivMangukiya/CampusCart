package com.parthiv.shopper.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parthiv.shopper.R;
import com.parthiv.shopper.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<com.parthiv.shopper.order.OrderAdapter.ContactsViewHolder> {

    public interface ItemClickListener {
        void onItemClick(long id);
    }

    private List<Order> itemList;
    private com.parthiv.shopper.item.ItemAdapter.ItemClickListener mItemClickListener;
    private Context mContext;

    public OrderAdapter(List<Order> itemList, com.parthiv.shopper.item.ItemAdapter.ItemClickListener itemClickListener,Context context) {
        this.itemList = itemList;
        mItemClickListener = itemClickListener;
        mContext = context;
    }

    @NonNull
    @Override
    public com.parthiv.shopper.order.OrderAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_item_item, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Order order = itemList.get(position);
        holder.nameTextView.setText(order.getUserName());
        holder.statusTextView.setText(order.getStatus());
        holder.amountTextView.setText(String.format("₹ %.2f",order.getAmount()));
        holder.mCardView.setOnClickListener((v)-> mItemClickListener.onItemClick(order.getId()));
        @ColorRes int id = 0;
        switch(order.getColor()){
            case 1:
                id = R.color.location_black;
                break;
            case 2:
                id = R.color.location_blue;
                break;
            case 3:
                id = R.color.location_red;
                break;
            case 4:
                id = R.color.location_green;
                break;
        }
        holder.mFrameLayout.setBackgroundColor(mContext.getResources().getColor(id));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView statusTextView;
        TextView amountTextView;
        FrameLayout mFrameLayout;
        CardView mCardView;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            statusTextView = itemView.findViewById(R.id.status);
            mCardView = itemView.findViewById(R.id.container);
            mFrameLayout = itemView.findViewById(R.id.colorFrame);
            amountTextView = itemView.findViewById(R.id.amount);
        }
    }

    public void updateList(List<Order> orders){
        itemList.clear();
        itemList.addAll(orders);
        notifyDataSetChanged();
    }

}


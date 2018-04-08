package com.parthiv.user.item;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parthiv.user.R;
import com.parthiv.user.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ContactsViewHolder> {

    public interface ItemClickListener {
        void onItemClick(long id);
    }

    private List<Item> itemList;
    private ItemClickListener mItemClickListener;

    public ItemAdapter(List<Item> itemList,ItemClickListener itemClickListener) {
        this.itemList = itemList;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_item_item, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.categoryTextView.setText(item.getCategory());
        holder.priceTextView.setText(String.format("₹ %.2f",item.getPrice()));
        holder.mCardView.setOnClickListener((v)-> mItemClickListener.onItemClick(item.getId()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView categoryTextView;
        TextView priceTextView;
        CardView mCardView;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            categoryTextView = itemView.findViewById(R.id.category);
            mCardView = itemView.findViewById(R.id.container);
            priceTextView = itemView.findViewById(R.id.price);
        }
    }

    public void updateList(List<Item> items){
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }

}


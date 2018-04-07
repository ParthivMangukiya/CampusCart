package com.parthiv.shopper.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parthiv.shopper.R;
import com.parthiv.shopper.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ContactsViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_item_item, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Item contacts = itemList.get(position);
        holder.nameTextView.setText(contacts.getName());
        holder.categoryTextView.setText(contacts.getCategory());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView categoryTextView;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            categoryTextView = itemView.findViewById(R.id.category);
        }
    }

    public void updateList(List<Item> items){
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }

}


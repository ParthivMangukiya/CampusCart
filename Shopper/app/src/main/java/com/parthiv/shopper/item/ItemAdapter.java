package com.parthiv.shopper.item;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parthiv.shopper.R;
import com.parthiv.shopper.model.Item;

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

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.categoryTextView.setText(item.getCategory());
        holder.mConstraintLayout.setOnClickListener((v)-> mItemClickListener.onItemClick(item.getId()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView categoryTextView;
        ConstraintLayout mConstraintLayout;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            categoryTextView = itemView.findViewById(R.id.category);
            mConstraintLayout = itemView.findViewById(R.id.container);
        }
    }

    public void updateList(List<Item> items){
        itemList.clear();
        itemList.addAll(items);
        notifyDataSetChanged();
    }

}


package com.parthiv.shopper.item;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.parthiv.shopper.R;
import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.model.Item;

public class ItemAddActivity extends AppCompatActivity {

    EditText nameText;
    EditText descriptionText;
    EditText categoryText;
    Button addButton;
    ConstraintLayout mConstraintLayout;
    ItemAddActivityViewModel mItemAddActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        nameText = findViewById(R.id.nameEdit);
        descriptionText = findViewById(R.id.descriptionEdit);
        categoryText = findViewById(R.id.categoryEdit);
        mConstraintLayout = findViewById(R.id.container);
        addButton = findViewById(R.id.addButton);
        mItemAddActivityViewModel = ViewModelProviders.of(this).get(ItemAddActivityViewModel.class);
        addButton.setOnClickListener((v) -> {
            String name = nameText.getText().toString();
            String description = descriptionText.getText().toString();
            String category = categoryText.getText().toString();
            mItemAddActivityViewModel.postItemLiveData(new Item(0,name,description,category,"")).observe(this,
                    jsonObjectResource -> {
                        if(jsonObjectResource.status == Resource.Status.SUCCESS) {
                            Snackbar.make(nameText, R.string.item_added, Snackbar.LENGTH_LONG).show();
                            nameText.setText("");
                            descriptionText.setText("");
                            categoryText.setText("");
                        }else{
                            Snackbar.make(nameText, "failed to add", Snackbar.LENGTH_LONG).show();
                        }
                    });
        });
    }
}

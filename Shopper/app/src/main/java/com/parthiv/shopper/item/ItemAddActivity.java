package com.parthiv.shopper.item;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parthiv.shopper.R;
import com.parthiv.shopper.Utils.Resource;
import com.parthiv.shopper.model.Item;

import java.io.IOException;
import java.util.UUID;

public class ItemAddActivity extends AppCompatActivity {

    EditText nameText;
    EditText descriptionText;
    EditText categoryText;
    EditText priceText;
    Button addButton;
    Button uploadButton;
    ConstraintLayout mConstraintLayout;
    ItemAddActivityViewModel mItemAddActivityViewModel;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ImageView imageView;
    private Uri filePath;
    private String uri = "";

    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        nameText = findViewById(R.id.nameEdit);
        descriptionText = findViewById(R.id.descriptionEdit);
        categoryText = findViewById(R.id.categoryEdit);
        mConstraintLayout = findViewById(R.id.container);
        addButton = findViewById(R.id.addButton);
        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadImage);
        priceText = findViewById(R.id.priceEdit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mItemAddActivityViewModel = ViewModelProviders.of(this).get(ItemAddActivityViewModel.class);
        addButton.setOnClickListener((v) -> {
            String name = nameText.getText().toString();
            String description = descriptionText.getText().toString();
            String category = categoryText.getText().toString();
            double price = Double.parseDouble(priceText.getText().toString());
            mItemAddActivityViewModel.postItemLiveData(new Item(0,name,description,category,uri,price)).observe(this,
                    jsonObjectResource -> {
                        if(jsonObjectResource.status == Resource.Status.SUCCESS) {
                            Toast.makeText(this, R.string.item_added, Toast.LENGTH_LONG).show();
                            nameText.setText("");
                            descriptionText.setText("");
                            categoryText.setText("");
                            priceText.setText("");
                        }else{
                            Toast.makeText(this, "Failed to Add", Toast.LENGTH_LONG).show();
                        }
                    });
        });
        uploadButton.setOnClickListener((v)->{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private String uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());


            uri = ref.toString();

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri ur) {
                                    uri = ur.toString();
                                }
                            });
                            Toast.makeText(ItemAddActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ItemAddActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            return ref.toString();
        }
        return null;
    }

}

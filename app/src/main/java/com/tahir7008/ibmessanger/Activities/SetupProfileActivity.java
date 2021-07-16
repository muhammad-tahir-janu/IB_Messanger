package com.tahir7008.ibmessanger.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tahir7008.ibmessanger.Models.User;
import com.tahir7008.ibmessanger.databinding.ActivitySetupProfileBinding;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    ProgressDialog dialog;

    Uri selectedImage;

    public final int IMG_REQUEST=45;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog =  new ProgressDialog(this);
        dialog.setMessage("Updating Profile");
        dialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        getSupportActionBar().hide();
        binding.ivSetUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMG_REQUEST);
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.etGetUserName.getText().toString();
                if(name.isEmpty()){
                    binding.etGetUserName.setError("Name Can't be Empty");
                    return;
                }
                dialog.show();
                if(selectedImage != null){
                    StorageReference storageReference = firebaseStorage.getReference().child("Profiles")
                            .child(firebaseAuth.getUid());
                    storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri =uri.toString();
                                        String uID =firebaseAuth.getUid();
                                        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
                                        String name  = binding.etGetUserName.getText().toString();
                                        User user = new User(name,uID,phoneNumber,imageUri);

                                        firebaseDatabase.getReference()
                                                .child("Users")
                                                .child(uID)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(SetupProfileActivity.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }else {
                    String uID =firebaseAuth.getUid();
                    String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();

                    User user = new User(name,uID,phoneNumber,"No Image");

                    firebaseDatabase.getReference()
                            .child("Users")
                            .child(uID)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SetupProfileActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            if(data.getData()!=null){
                binding.ivSetUserImage.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }

    }
}
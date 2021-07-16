package com.tahir7008.ibmessanger.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tahir7008.ibmessanger.Adapters.StatusAdapter;
import com.tahir7008.ibmessanger.Models.Status;
import com.tahir7008.ibmessanger.Models.UserStatus;
import com.tahir7008.ibmessanger.R;
import com.tahir7008.ibmessanger.Models.User;
import com.tahir7008.ibmessanger.Adapters.UsersAdapter;
import com.tahir7008.ibmessanger.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    FirebaseDatabase firebaseDatabase;
    ArrayList<User> userArrayList;
    ArrayList<UserStatus> userStatusArrayList;

    private final int STATUS_REQUEST_CODE=12;

    UsersAdapter myAdapter;

    StatusAdapter  statusAdapter ;

    ProgressDialog dialog;

    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        userArrayList = new ArrayList<User>();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Status");
        dialog.setCancelable(false);

        user = new User();
        firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        userStatusArrayList = new ArrayList<>();
        statusAdapter = new StatusAdapter(this,userStatusArrayList);
        binding.rvStatusList.setAdapter(statusAdapter);
        //binding.rvStatusList.setHasFixedSize(true);
        binding.rvStatusList.setLayoutManager(linearLayoutManager);


        myAdapter = new UsersAdapter(this,userArrayList);
        binding.rvChat.setAdapter(myAdapter);
        binding.rvChat.setHasFixedSize(true);


        firebaseDatabase.getReference().child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        userArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            User user  = dataSnapshot.getValue(User.class);
                            userArrayList.add(user);
                        }
                        myAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch(item.getItemId()){
                    case R.id.status:
                        Intent intent = new Intent();
                        intent.setType("image/*")
                                .setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, STATUS_REQUEST_CODE);
                }
                return false;
            }
        });






    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData()!=null){
                dialog.show();
                Date date = new Date();
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference reference = firebaseStorage.getReference().child("status")
                        .child(date.getTime()+ " ");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserStatus userStatus = new UserStatus();
                                userStatus.setName(user.getName());
                                userStatus.setProfileImg(user.getProfileImage());
                                userStatus.setLastUpdate(date.getTime());

                                Status status = new Status();


                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("name",userStatus.getName());
                                hashMap.put("profileImage",userStatus.getProfileImg());
                                hashMap.put("lastUpdated",userStatus.getLastUpdate());


                                firebaseDatabase.getReference()
                                        .child("stories")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .updateChildren(hashMap);


                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Toast.makeText(getApplicationContext(), "Setting Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.invite:
                Toast.makeText(this, "invite Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.groups:
                Toast.makeText(this, "Group Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
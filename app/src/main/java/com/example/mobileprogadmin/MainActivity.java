package com.example.mobileprogadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    OrderAdapter orderAdapter;
    RecyclerView orderRecycler;
    DatabaseReference mRef;
    ArrayList<String> itemsModelArrayList;
    ArrayList<OrderModel> orderModelArrayList;

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        orderRecycler = findViewById(R.id.orderRecycler);
        orderModelArrayList = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        fetchFirebaseData();
    }

    private void fetchFirebaseData() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot uSnap : snapshot.getChildren()){
                    for(DataSnapshot oSnap : uSnap.child("orders").getChildren()){
                        itemsModelArrayList = new ArrayList<>();
                        for(DataSnapshot nameSnap : oSnap.child("itemsModelArrayList").getChildren()){
                            String itemsModel = nameSnap.getValue().toString();
                            itemsModelArrayList.add(itemsModel);
                        }
                        OrderModel orderModel = new OrderModel();
                        orderModel.setGateTxt(oSnap.child("gateTxt").getValue().toString());
                        orderModel.setTimeTxt(oSnap.child("timeTxt").getValue().toString());
                        orderModel.setPhoneNumberTxt(oSnap.child("phoneNumberTxt").getValue().toString());
                        orderModel.setStatusTxt(oSnap.child("statusTxt").getValue().toString());
                        orderModel.setOrderPriceTxt(oSnap.child("orderPriceTxt").getValue().toString());
                        orderModel.setItemsModelArrayList(itemsModelArrayList);
                        orderModelArrayList.add(orderModel);
                    }

                }

                orderAdapter = new OrderAdapter(orderModelArrayList,getApplicationContext());
                orderRecycler.setLayoutManager(linearLayoutManager);
                orderRecycler.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.mobileprogadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    List<OrderModel> orderModelList;
    Context context;
    DatabaseReference mRef;

    public OrderAdapter(List<OrderModel> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        holder.gateTxt.setText(orderModelList.get(position).getGateTxt());
        holder.timeTxt.setText(orderModelList.get(position).getTimeTxt());
        holder.statusTxt.setText(orderModelList.get(position).getStatusTxt());
        holder.orderPriceTxt.setText(orderModelList.get(position).getOrderPriceTxt());

        ItemsAdapter itemsAdapter;
        itemsAdapter = new ItemsAdapter(orderModelList.get(position).getItemsModelArrayList(),context);
        holder.itemsRecycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        holder.itemsRecycler.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef = FirebaseDatabase.getInstance().getReference().child("users");

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot uSnap : snapshot.getChildren()){
                            for(DataSnapshot mSnap : uSnap.child("orders").getChildren()){
                                ArrayList<String> itemsModelFirebase;
                                itemsModelFirebase = (ArrayList<String>) mSnap.child("itemsModelArrayList").getValue();
                                if(orderModelList.get(holder.getAdapterPosition()).getItemsModelArrayList().equals(itemsModelFirebase)){
                                    mSnap.child("statusTxt").getRef().setValue(holder.statusTxt.getText().toString());
                            }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeTxt;
        TextView gateTxt;
        RecyclerView itemsRecycler;
        TextView orderPriceTxt;
        EditText statusTxt;
        Button updateBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTxt = itemView.findViewById(R.id.timeTxt);
            gateTxt = itemView.findViewById(R.id.gateTxt);
            itemsRecycler = itemView.findViewById(R.id.itemsRecycler);
            orderPriceTxt =  itemView.findViewById(R.id.orderPriceTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            updateBtn = itemView.findViewById(R.id.updateBtn);

        }
    }
}

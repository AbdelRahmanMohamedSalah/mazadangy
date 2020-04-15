package com.mazad.mazadangy.gui.followAction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mazad.mazadangy.R;
import com.mazad.mazadangy.adapter.FollowActionAdapter;
import com.mazad.mazadangy.model.Auction;

import java.util.ArrayList;

public class FollowActionActivity extends AppCompatActivity {
    androidx.recyclerview.widget.RecyclerView recyclerView_dialog_follow_auction;
    FollowActionAdapter auctionAdapter;
    ArrayList<Auction> arrayAuction;
    Auction action;
    DatabaseReference DR_setEndPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_follow_action);

        Intent intent = this.getIntent();
        String id_post = intent.getStringExtra("id_Post");
        String fromActivity = intent.getStringExtra("from_activity");
        System.out.println("id post = "+id_post);

        if (fromActivity.equals("posts")) {
            DR_setEndPrice = FirebaseDatabase.getInstance().getReference("posts").child(id_post + "");


        } else {

            DR_setEndPrice = FirebaseDatabase.getInstance().getReference("mony_post").child(id_post + "");

        }


        arrayAuction = new ArrayList<>();
        action = new Auction();


        DR_setEndPrice.child("auction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    action = dataSnapshot1.getValue(Auction.class);
                    System.out.println(dataSnapshot1+"");
                    arrayAuction.add(action);
                     auctionAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView_dialog_follow_auction = findViewById(R.id.recycleFollowPostDetailsActivity);

        auctionAdapter = new FollowActionAdapter(FollowActionActivity.this, arrayAuction);

        recyclerView_dialog_follow_auction.setLayoutManager(new LinearLayoutManager(FollowActionActivity.this));

        recyclerView_dialog_follow_auction.setAdapter(auctionAdapter);
        auctionAdapter.notifyDataSetChanged();


    }
}

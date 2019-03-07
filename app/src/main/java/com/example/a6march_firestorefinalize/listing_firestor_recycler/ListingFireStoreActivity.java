package com.example.a6march_firestorefinalize.listing_firestor_recycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a6march_firestorefinalize.ContactUser;
import com.example.a6march_firestorefinalize.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListingFireStoreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ContactUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_fire_store);

        recyclerView = findViewById(R.id.recyclerID);

        setupRecycler();

    }

    private void setupRecycler() {

        Query query = FirebaseFirestore.getInstance().collection("employee").orderBy("phone",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ContactUser> options =new FirestoreRecyclerOptions.Builder<ContactUser>()
                .setQuery(query,ContactUser.class)
                .build();

        adapter = new ContactUserAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(ListingFireStoreActivity.this));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setHasFixedSize();
        recyclerView.setAdapter(adapter);



    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}

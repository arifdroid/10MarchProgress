package com.example.a6march_firestorefinalize.score_your_friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.a6march_firestorefinalize.R;
import com.example.a6march_firestorefinalize.listing_firestor_recycler.ListingFireStoreActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Score_Friend_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ScoreFriendAdapter adapter;



    //we get firestore to retrieve, name and image from document

    //we pass by intent, but lets pretend its passed.

    private String company_phone="company_uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score__friend_);

        recyclerView = findViewById(R.id.recycler_activity_score_ID);

        setupRecycler();

     //   populateFromFireStore();

    }

    private void setupRecycler() {

        CollectionReference collectionReference_of_company =
                FirebaseFirestore.getInstance().collection("employees_to_offices")
                        .document(company_phone).collection("uid_employee_this");

        Query query1 = collectionReference_of_company.orderBy("name",Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<ScoreFriend> options = new FirestoreRecyclerOptions.Builder<ScoreFriend>()
                .setQuery(query1,ScoreFriend.class)
                .build();


        adapter = new ScoreFriendAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(Score_Friend_Activity.this));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setHasFixedSize();
        recyclerView.setAdapter(adapter);

        Log.i("checkk size test",""+ ScoreFriendAdapter.test.size());

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

    private void populateFromFireStore() {

        //document company, to retrieve all employee
        CollectionReference collectionReference_of_company =
                FirebaseFirestore.getInstance().collection("employees_to_offices")
                .document(company_phone).collection("uid_employee_this");

        //get all name and score card reference



    }
}

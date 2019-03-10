package com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a6march_firestorefinalize.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Write_ScoreCard_Activity extends AppCompatActivity {

    boolean finish_load_firestore;

    private ArrayList<FireStoreSQLiteClass> listt= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write__score_card_);

        finish_load_firestore=false;

        loadFromFireStore();


        //this cant be here, since data might not finish process,
        //this might execute before we even return the boolean result.

        //either process direct in load and wait, or, we introduce listener using own interface?



//        if(finish_load_firestore==true) { //this is triggered when we finish load our data, let try easy one first
//            createSQlite();
//        }
//


    }

    private void loadFromFireStore() {


        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("employees_to_offices").document("company_uid")
                .collection("uid_employee_this");

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                //just double check if, by chance, when registered, document not created, somehow

                int size = queryDocumentSnapshots.size();

                Map<String, Object> mapp = new HashMap<>()

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    //here we loop throught all document,
                    //remember we want references to other coworker doc
                    //so we dont need our own ref, dont create table of our own
                    //retrieve the name, picture (blob),phone,

                mapp= documentSnapshot.getData();   //get all data in from of map

                //remap work
                //FireStoreSQLiteClass instance = new FireStoreSQLiteClass();

                    String phoneHere = "";
                    String nameHere = "";
                    String imageHere = "";
                    String refScoreHere = "";


                for(Map.Entry<String,Object> kk:mapp.entrySet()){

                    if(kk.getKey().equals("name")){
                        nameHere = (kk.getValue().toString());
                    }

                    if(kk.getKey().equals("phone")){
                        phoneHere = (kk.getValue().toString());
                    }

                    if(kk.getKey().equals("image_url")){
                        imageHere = kk.getValue().toString();
                    }
                    if(kk.getKey().equals("my_score_ref")){
                        refScoreHere = kk.getValue().toString();
                    }

                    if(nameHere!=null && phoneHere!=null&& refScoreHere!=null&&imageHere==null){

                        //we dont want to add two times if image is not here.
                        listt.add(new FireStoreSQLiteClass(nameHere,phoneHere,refScoreHere));

                    }else if(nameHere!=null && phoneHere!=null&& refScoreHere!=null&&imageHere!=null){
                        listt.add(new FireStoreSQLiteClass(nameHere,phoneHere,refScoreHere,imageHere));
                    }

                }



                }



            }
        })






        if(finish_load_firestore==true){
            createdatabase();
        }

    }
}

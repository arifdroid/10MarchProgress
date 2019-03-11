package com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a6march_firestorefinalize.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Write_ScoreCard_Activity extends AppCompatActivity {

    boolean finish_load_firestore;

    private ArrayList<FireStoreSQLiteClass> listt= new ArrayList<>();

    private FS_SQLite_DBHelper fs_sqLite_dbHelper;

    private List<FireStoreSQLiteClass> returnedList;

    private RecyclerView recyclerView;

    private RecyclerCustomAdapter recyclerAdapter;

    private static ArrayList<FireStoreSQLiteClass> result;

    private Button sendButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write__score_card_);

        finish_load_firestore=false;
        sendButton=findViewById(R.id.booottoonID);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(returnedList!=null){

                    Toast.makeText(Write_ScoreCard_Activity.this,"list here", Toast.LENGTH_LONG).show();

                    for(int j=0;j<returnedList.size();j++) {

                        Log.i("checkk finally: ", j+" name"+ returnedList.get(j).getName() + " rating: "+ returnedList.get(j).getRating());
                    }
                }

            }
        });


       // ratingTest=new ArrayList<FireStoreSQLiteClass>("name","score ref",0.5);

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
                int counterDocument=0;

                Map<String, Object> mapp;

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    counterDocument++;
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

                int o = 0;
                for(Map.Entry<String,Object> kk:mapp.entrySet()){
                       o++;
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
                    Log.i("checkk o : ", ""+ o);



                if((nameHere!="" && phoneHere!=""&& refScoreHere!=""&&imageHere!="")||(nameHere!="" && phoneHere!=""&&refScoreHere!="")){
                        listt.add(new FireStoreSQLiteClass(nameHere,phoneHere,refScoreHere,imageHere));
                        Log.i("checkk oV3 : ", ""+ o);
                    }

                }


                }
                listt.size();


                //then we can check here if, count == document size

                if(size==counterDocument){

                    Log.i("finish load"," load from firestore");

                    createdatabase();



                }










            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.i("checkk fail : ", "fail load firestore"+ e.getMessage());
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                Log.i("checkk cancel : ", "cancel load firestore " );
            }
        });








    }

    private void createdatabase() {

        if(listt.size()>0){


            for(int j=0;j<listt.size();j++){

                Log.i("checkk data : ", "name "+j+" "+ listt.get(j).getName()+"\n");
                Log.i("checkk data : ", "phone "+j+" "+ listt.get(j).getPhone());
                Log.i("checkk data : ", "imageURL "+j+" "+ listt.get(j).getImage_url());
                Log.i("checkk data : ", "score ref "+j+" "+ listt.get(j).getMy_score_ref());



            }


        }

        //instance



        FS_SQLite_DBHelper fs_sqLite_dbHelperNEW = FS_SQLite_DBHelper.getInstance(this,listt);

        returnedList = fs_sqLite_dbHelperNEW.getAll_FSToSQLite();
        //here try setup database
        //fs_sqLite_dbHelper = new FS_SQLite_DBHelper(this, listt);

        //here getting the data all back


        drawList();

    }

    private void drawList(){



        //we got the data, we can call adapter recycler list, to try

        recyclerView = findViewById(R.id.recyler_writeScoreCardActivity_ID);

        recyclerAdapter = new RecyclerCustomAdapter(Write_ScoreCard_Activity.this, returnedList);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setPassingResultInterface(new PassingResultInterface() {
            @Override
            public void passingArray(ArrayList<FireStoreSQLiteClass> fireStoreSQLiteClasses) {
                returnedList=fireStoreSQLiteClasses;
            }
        });



    }


}

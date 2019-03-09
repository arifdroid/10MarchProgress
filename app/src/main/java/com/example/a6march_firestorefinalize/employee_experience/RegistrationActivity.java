package com.example.a6march_firestorefinalize.employee_experience;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a6march_firestorefinalize.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class RegistrationActivity extends AppCompatActivity {

    //fields for registration process
    private Button button_signin, button_getCode;
    private EditText editText_employee_name, editText_employee_phone
            ,editText_company_phone, editText_code;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    //fields for code
    private String codeFirebase_Sent;


    //fields for message
    private TextView textViewMessageRegistration;

    //fields for entered company uid, phone
    private String company_phone_user_enter;

    //noticing that firestore do not get data instantly, so
    //it will be awhile before checking customer is already in database or not
    //so we need to instantiate it always false,

    //3 conditions, 1) always false until we get data >adminsOffices<collection saying it exist.
                    // if this true , then need to check, got document already or not
    //              2)  until we get data >employees_to_offices<document>uid_employee_this<collection
    //              3) revert? always true, else, we create?  TRUE means we assume already exist. just condition change.

   // private boolean userRegistered; //this is always false, user is not registered, until proven otherwise

   // private boolean docExist;   //this is always true, assume if user registered, doc exist, just wait to prove, if otherwise add new doc

    private String nameRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //existiantial crisis
//        userRegistered=false;
//        docExist=true;

    //edit text input
    editText_code= findViewById(R.id.editText_registration_employee_code_ID);

    editText_company_phone=findViewById(R.id.editText__registration_companyPhone_ID);
    editText_employee_name = findViewById(R.id.editTextName_registrationID);
    editText_employee_phone = findViewById(R.id.editText_registration_phone_employee_ID);

    //button ui
    button_getCode=findViewById(R.id.button_registration_code_ID);
    button_signin = findViewById(R.id.button_registration_signIn_ID);

    //textview message
    textViewMessageRegistration = findViewById(R.id.textView_registration_ID);
    textViewMessageRegistration.setText("...");

        FirebaseUser userInit = FirebaseAuth.getInstance().getCurrentUser();

        //sign out if currently got user log in
        if(userInit!=null){
            FirebaseAuth.getInstance().signOut();
        }

    button_signin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //if receive text, then enter.
            String name = editText_employee_name.getText().toString();
            String company_phone = editText_company_phone.getText().toString();
            String your_number = editText_employee_phone.getText().toString();
            String codehere = editText_code.getText().toString();

            company_phone_user_enter=company_phone;

            if((name!=null)&&(company_phone!=null)&&(your_number!=null)) {

                checkCredential(codeFirebase_Sent,codehere);
            }



        }
    });

    button_getCode.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            textViewMessageRegistration.setText("starting verification");

            getCallbackPhoneAuth();

        }
    });

    mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            textViewMessageRegistration.setText("getting verification..");

            //make sure user enter all information

            String name = editText_employee_name.getText().toString();
            String company_phone = editText_company_phone.getText().toString();
            String your_number = editText_employee_phone.getText().toString();

            company_phone_user_enter=company_phone;

            if((name!=null)&&(company_phone!=null)&&(your_number!=null)) {
                gettingVerification(phoneAuthCredential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            textViewMessageRegistration.setText("verification failed");
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

         //received text verification

          textViewMessageRegistration.setText("code received");
          codeFirebase_Sent =s;

        }
    };


    }

    private void checkCredential(String codeFirebase_sent, String codehere) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeFirebase_sent,codehere);

            gettingVerification(credential);
    }

    private void gettingVerification(PhoneAuthCredential phoneAuthCredential) {



        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //need to check of admin document exist first.



                    CollectionReference collectionReference_checkaadmin_exist = FirebaseFirestore.getInstance().collection("adminsOffices");
//
                    Query query_checkadmin = collectionReference_checkaadmin_exist.whereEqualTo("phone_admin",company_phone_user_enter);
//
                    query_checkadmin.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                            //means if size > 1 , document exist.
                            if (queryDocumentSnapshots.size()>=1){
                                    Log.i("check ", "admin number recognized");


                    Log.i("checkk "," task is successful");

                    //test if user is registered by admin

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    final CollectionReference collectionReference_adminsOffices = FirebaseFirestore.getInstance().collection("adminsOffices");

                    final Query query = collectionReference_adminsOffices.whereArrayContains("employee_this_admin",user.getPhoneNumber());

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Map<String, Object> map;
                            int i =0;
                            int j =0;

                          //  ArrayList<String> checkUser = new ArrayList<>();

                            int size = queryDocumentSnapshots.size();

                            Log.i("checkk ", "stop 1");

                            Log.i("checkk flow ","stop 00");

                            for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){ //this cycle extract document with query

                                Log.i("checkk ", "stop 2");

                                Log.i("checkk flow ","stop 11");

                                map= documentSnapshot.getData(); //every single document consist of hashmap
                                 j++;

                                Log.i("checkk test "+j, "document count i think");

                                for(Map.Entry<String,Object> kk : map.entrySet()){
                                    i++;
                                    if(kk.getKey().equals("employee_this_admin")){


                                        Log.i("checkk test "+i, "test"+ kk.getValue().toString());
                                        //this will still return string
                                        //regex?
                                        //we dont have to do this, since if document exist, means user is registered, but real question is,

                                        //where to goes if user not exist, or document no present

                                        //if this exist, means user is registered by admin, so we can log in, but
                                        //before that, need to create document/update
                                        //so task is always succesfull even number user is not registered.
//                                        if(kk.getValue().toString().equals(user.getPhoneNumber())) {
//                                            userLoggedIn();
//                                        }

                                       // checkUser.add(kk.getValue()); //add all collected user
                                    }

                                }

                                nameRegistered = editText_employee_name.getText().toString();


                           checkIfAlreadyRegistered();  // user is registered by admin, by has user already registered before and got existing
                                //document

                            }


                            Log.i("checkk flow ","stop 22");
                            if(size<1){

                                Log.i("checkk flow ","stop 77");
                                Log.i("checkk", "no document present?");

                                logOutFromFirebaseAuth();
                            }
                            //need to check if no data, it will fail or still success


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            logOutFromFirebaseAuth();
                    Log.i("checkk ","error " + e.getMessage());

                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                            logOutFromFirebaseAuth();

                            Log.i("checkk ","cancel");

                        }
                    });

                }else {

                    Log.i("check ", "admin number not recognized");


                }

            }
        });


                } else {

                    //user not exist

                    logOutFromFirebaseAuth();

                    Log.i("checkk ORIGINAL TASK"," task not successful");

                }

            }
        });

    }

    private void logOutFromFirebaseAuth() {
        Log.i("checkk flow ","stop 88");

        FirebaseAuth.getInstance().signOut();
        Log.i("checkk","logged OUT");
    }

    private void userLoggedIn() {


        Log.i("checkk flow ","stop 99");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //create document with reference of phone number
    // we need to check whether admin exist or not, else dont proceed


    //if exist, probably already registered. dont log, in, ask to retrieve password.




    /// if document not exist, below

    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("employees_to_offices").document(company_phone_user_enter+"_company_uid")
            .collection("uid_employee_this").document(user.getPhoneNumber()+"final");

    //unique uid for scorecard, uid from user/employee document
    String score_card_uid = user.getPhoneNumber()+"score_card_uid";


    //create document for score card with reference from user uid.

    DocumentReference documentReference_score_uid = FirebaseFirestore.getInstance()
            .collection("employees_to_offices").document(company_phone_user_enter+"_company_uid")
                    .collection("score_ref_collection_this").document(score_card_uid);

    Map<String,Object> ll = new HashMap<>();

    ll.put("name",nameRegistered);
    ll.put("phone",user.getPhoneNumber());
    ll.put("my_score_ref",score_card_uid);

    documentReference.set(ll);

    Map<String,Object> hh = new HashMap<>(); //now how do we populate this, we populate here or later?

    hh.put("my_score","0");

    documentReference_score_uid.set(hh);

    Log.i("checkk uid"," score card "+score_card_uid );

    textViewMessageRegistration.setText("you are logged in");
    Log.i("checkk","logged IN");


    }

    private void checkIfAlreadyRegistered() {

        Log.i("checkk flow ","stop 33");

        CollectionReference collectionReference_check_ifResisteredAlready =
                FirebaseFirestore.getInstance().collection("employees_to_offices").document(company_phone_user_enter+"_company_uid")
                        .collection("uid_employee_this");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query queryhere = collectionReference_check_ifResisteredAlready.whereEqualTo("phone",user.getPhoneNumber());

        queryhere.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                int size = queryDocumentSnapshots.size();


                int i =0;

                Log.i("checkk flow 555","i : "+i);

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    i++;

                    Log.i("checkk  i flow ","i : "+i);

                    String s = documentSnapshot.getData().get("phone").toString();

                    Log.i("checkk flow document ","data is: "+s);

                }

                Log.i("checkk flow 666","i : "+i);

                if(size>=1){ //means user document already exist

                    Log.i("checkk flow ","stop 55");
                    logOutFromFirebaseAuth_Already_registered();

                }else {
                    Log.i("checkk flow ","stop 66");
                    userLoggedIn();
                }
            }
        });

    }

    private void logOutFromFirebaseAuth_Already_registered() {

        FirebaseAuth.getInstance().signOut();
        Log.i("checkk","already registered, please contact admin");

    }

    private void getCallbackPhoneAuth() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(editText_employee_phone.getText().toString(),
                60,
                TimeUnit.SECONDS,
                RegistrationActivity.this,
                mCallback
                );

    }
}

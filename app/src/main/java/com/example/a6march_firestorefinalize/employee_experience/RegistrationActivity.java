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
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ListIterator;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    //edit text input
    editText_code= findViewById(R.id.editTextCodeID);
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
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){



                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //here we want to verify this current user is registered by admin by some office

                    final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("adminsOffices");

                    //extract the company phone, and go to company document from collection,, this

                    //BUT THIS WILL CREATE, we dont want user to create,, need to check
                    //lets just use query?
                        //final String companyNumber = company_phone_user_enter; ////

                    //5.06pm >> change back to employee number inside company

                    final String currentEmployeeIfExist = user.getPhoneNumber();

                    Query query = collectionReference.whereArrayContains("employee_this_admin",currentEmployeeIfExist);

                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()){

                                textViewMessageRegistration.setText("getting admin data..");

                                Log.i("checkk", "exist "+ currentEmployeeIfExist);

                                //this should means, user is registered,verified from admin.



                                //now we write this user document in another collections.

                                final CollectionReference collectionReference_uid_employee_this =
                                        FirebaseFirestore.getInstance().collection("employees_to_offices").document(company_phone_user_enter)
                                        .collection("uid_employee_this");

                                //check whether user exist in document

                                Query query_uid_employee_this = collectionReference_uid_employee_this.whereEqualTo("phone",user.getPhoneNumber());

                                query_uid_employee_this.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                        if(task.isSuccessful()){
                                            //dont add document

                                            textViewMessageRegistration.setText("already registered");
                                        }else {

                                            //create document for this user

                                            String namehere = editText_employee_name.getText().toString();
                                            String uidsetuphere_for_score_ref = user.getUid();

                                            collectionReference_uid_employee_this.document(user.getPhoneNumber()).set(new Employee_Details(namehere,user.getPhoneNumber(),uidsetuphere_for_score_ref));

                                             textViewMessageRegistration.setText(namehere+" database created");

                                             Log.i("checkk database","success"+ user.getPhoneNumber());
                                        }
                                    }
                                });




                            userLoggedIn();


                            }else {
                                Log.i("checkk dont belong", "error "+user.getPhoneNumber());
                                textViewMessageRegistration.setText("user dont exist anywhere");

                                logOutFromFirebaseAuth();
                            }

                        }
                    });


                }else {

                    //task is not succesful, fail to verify credential
                    //so we log out user from firebase auth

                    logOutFromFirebaseAuth();
                }

            }
        });

    }

    private void logOutFromFirebaseAuth() {

        FirebaseAuth.getInstance().signOut();
    }

    private void userLoggedIn() {

    textViewMessageRegistration.setText("you are logged in");


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

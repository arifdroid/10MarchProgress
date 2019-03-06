package com.example.a6march_firestorefinalize;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button buttonSignIn,buttonCode, buttonNextActivity;

    private EditText editTextName,editTextPhone,editTextCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    private TextView textViewMessage;

    private String codeReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        buttonSignIn = findViewById(R.id.buttonSignInID);
        buttonCode = findViewById(R.id.buttonGetCodeID);

        buttonNextActivity = findViewById(R.id.nextActivityID);

        editTextName = findViewById(R.id.editTextNameID);
        editTextPhone = findViewById(R.id.editTextPhoneID);
        editTextCode = findViewById(R.id.editTextCodeID);
        textViewMessage = findViewById(R.id.textViewMessageID);

        buttonNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                verifyCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                textViewMessage.setText(e.getMessage());

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                codeReceived=s;
                textViewMessage.setText("code received");

            }
        };

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(codeReceived!=null){

                    textViewMessage.setText("enter code");
                    String codeR = codeReceived;
                    String codeMe =editTextCode.getText().toString();


                    getCredential(codeR,codeMe);
                }

            }
        });

        buttonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewMessage.setText("get Code");
                getCallback();

            }
        });


    }

    private void getCredential(String codeR, String codeMe) {



        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeMe,codeR);
        verifyCredential(credential);

    }

    private void verifyCredential(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("employee");

                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<String> checkPhone = new ArrayList<>();
                            if(task.isSuccessful()){

                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){

                                    checkPhone.add(queryDocumentSnapshot.getString("phone"));

                                    Log.i("checkk phone ",queryDocumentSnapshot.getString("phone"));
                                }

                                if(!checkPhone.contains(user.getPhoneNumber())){
                                    //now we add to database if no record of current user from authentication

                                    ContactUser kk = new ContactUser();
                                    kk.setName(editTextName.getText().toString());
                                    kk.setPhone(user.getPhoneNumber());
                                    kk.setUid(user.getUid()); //realize this is not the document uid, this is the
                                        //uid from user authentication page
                                    collectionReference.document().set(kk);

                                }

                            }

                        }
                    });



                    userIsLoggedIn();

                }


            }

        });

    }

    private void userIsLoggedIn() {

            textViewMessage.setText("user is logged in");

            buttonNextActivity.setVisibility(View.VISIBLE);

    }

    private void getCallback() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(editTextPhone.getText().toString(),
                40,
                TimeUnit.SECONDS,
                this,
                mCallback);

    }
}

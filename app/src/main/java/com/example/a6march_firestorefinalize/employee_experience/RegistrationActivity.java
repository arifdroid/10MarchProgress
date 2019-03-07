package com.example.a6march_firestorefinalize.employee_experience;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

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
      textViewMessageRegistration.setText("code received");
      codeFirebase_Sent =s;

        }
    };


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

                    collectionReference.document(company_phone_user_enter).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            if(task.isSuccessful()){




                            }else {
                                //this either means user enter wrong number, document dont
                                textViewMessageRegistration.setText("company phone not exist");
                            }
                        }
                    });


                }else {
                    textViewMessageRegistration.setText("fail verify credential..");
                }

            }
        });

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

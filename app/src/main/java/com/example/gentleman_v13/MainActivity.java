package com.example.gentleman_v13;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        final Button GuestBtn = (Button) findViewById(R.id.GuestBtn);
        final Button SignUpBtn = (Button) findViewById(R.id.SignUpBtn);
        final Button LoginBtn  = (Button) findViewById(R.id.LoginBtn);


        Map<String, Object> user = new HashMap<>();


//        Guest Btn
        GuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent MainPage = new Intent(getApplicationContext(),MainPage.class);
                startActivity(MainPage);
            }
        });


//        SignUp Btn
        SignUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent SignUpPage= new Intent(getApplicationContext(),SignUpPage.class);
                startActivity(SignUpPage);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Login Btn
        LoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
//                Intent LoginPage= new Intent(getApplicationContext(),LoginPage.class);
//                startActivity(LoginPage);
                user.put("FirstName", "Suhib");
                user.put("LastName", "Alabed");
                user.put("Email", "suhib.alabed@gmail.com");
                user.put("Age", 23);
                user.put("PhoneNumber", "+962 778870791");

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast toast = Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
    }
}

//----------------Read Data----------------------

//db.collection("users")
//        .get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//@Override
//public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        if (task.isSuccessful()) {
//        for (QueryDocumentSnapshot document : task.getResult()) {
//        Log.d(TAG, document.getId() + " => " + document.getData());
//        }
//        } else {
//        Log.w(TAG, "Error getting documents.", task.getException());
//        }
//        }
//        });

//----------------Add Data----------------------

//    Map<String, Object> user = new HashMap<>();
//user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//
//// Add a new document with a generated ID
//        db.collection("users")
//        .add(user)
//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Log.w(TAG, "Error adding document", e);
//        }
//        });
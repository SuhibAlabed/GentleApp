package com.example.gentleman_v13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "AdminPage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        mAuth = FirebaseAuth.getInstance();
    }

    //   When Start App
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getData(currentUser.getUid());
//            Toast.makeText(getApplicationContext(), currentUser.getUid(),
//                    Toast.LENGTH_SHORT).show();
        }else{
//            UserName.setText("Guest");
        }

    }

    public void getData(String UserId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("UserId", UserId)
                .orderBy("Role", Query.Direction.ASCENDING)
                .orderBy("Email", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                UserName.setText(document.getString("FullName"));
                            }
                        } else {
                            Intent MainActivity= new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(MainActivity);
                        }
                    }
                });
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent MainActivity= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(MainActivity);
    }
}


package com.example.gentleman_v13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpPage";
    EditText email;
    EditText password;
    EditText name;
    EditText age;
    EditText phoneNumber;
    Map<String, Object> user = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        mAuth = FirebaseAuth.getInstance();

        name        = (EditText) findViewById(R.id.editTextTextPersonName);
        age         = (EditText) findViewById(R.id.editTextNumber);
        email       = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password    = (EditText) findViewById(R.id.editTextTextPassword);
        phoneNumber = (EditText) findViewById(R.id.editTextPhone);

    }


    public void SignUpBtn(View view) {
        String email1           = email.getText().toString();
        String password1       = password.getText().toString();
        String name1           = name.getText().toString();
        String age1            = age.getText().toString();
        String phoneNumber1    = phoneNumber.getText().toString();
        if(!email1.equals("") && !password1.equals("") && !name1.equals("") && !age1.equals("") && !phoneNumber1.equals("")){
            mAuth.createUserWithEmailAndPassword(email1, password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                CreateFireStore(email1,user.getUid(),name1,age1,phoneNumber1);
                                Toast.makeText(getApplicationContext(), "User Created Successfully",
                                        Toast.LENGTH_SHORT).show();
                                RemoveInputValue();
                                Intent LoginPage= new Intent(getApplicationContext(),LoginPage.class);
                                startActivity(LoginPage);
//                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Fill all field",
                    Toast.LENGTH_SHORT).show();
        }

    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void CreateFireStore(String email,String UserId,String name, String age,String phoneNumber){
        user.put("FullName", name);
        user.put("UserId",UserId);
        user.put("Email", email);
        user.put("Age", age);
        user.put("PhoneNumber", phoneNumber);

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
    public void RemoveInputValue(){
        email.setText("");
        password.setText("");
        name.setText("");
        age.setText("");
        phoneNumber.setText("");
    }
}

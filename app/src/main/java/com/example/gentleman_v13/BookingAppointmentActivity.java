package com.example.gentleman_v13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;




import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookingAppointmentActivity extends AppCompatActivity {
//    private FirebaseAuth mAuth;
    GridView gridView;
    Button BookAppointmentBtn;
    ArrayAdapter<String> adapter;

    private RadioGroup radioGroup;
    EditText pkDate;
    TextView ServiceTxt;
    TextView NoTimeAva;
    private String Service;
    private static final String TAG = "BookingAppointmentActivity";
    Map<String, Object> AvailableSlot     = new HashMap<>();
    Map<String,Object>  Booking           = new HashMap<>();
    Map<String,Object>  BookingDetails    = new HashMap<>();
    ArrayList<String>   listAvailableSlot = new ArrayList<String>();
//    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_appointment);


        NoTimeAva         = findViewById(R.id.no_time_available);
        pkDate            = findViewById(R.id.editDatePicker);
        ServiceTxt        = findViewById(R.id.ServiceTxt);
        Service           = getIntent().getStringExtra("Service");

        Calendar calendar = Calendar.getInstance();
        final int year    = calendar.get(Calendar.YEAR);
        final int month   = calendar.get(Calendar.MONTH);
        final int day     = calendar.get(Calendar.DAY_OF_MONTH);

        ServiceTxt.setText(Service);
        pkDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingAppointmentActivity.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day +"-"+ month + "-" +year;
                        pkDate.setText(date);
                        removeRadioGroup();
                        createAvailableSlot(pkDate.getText().toString(), (String) ServiceTxt.getText());
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//              datePickerDialog.getDatePicker().setMaxDate(calendar);
                datePickerDialog.show();
            }
        });

    }

    private void createAvailableSlot(String Date,String Service) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AvailableSlot")
                .whereEqualTo("Date", Date)
                .orderBy("Service", Query.Direction.ASCENDING)
                .orderBy("Time",Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() ) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    AvailableSlot= document.getData();
                                    NoTimeAva.setText("");
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    assert AvailableSlot != null;
                                    createRadioGroup((Map<String, Object>) AvailableSlot.get("Time"));
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    @SuppressLint("ResourceType")
    private void createRadioGroup(Map<String,Object> availableSlot) {
//        Log.d(TAG, "Available Slot: " + availableSlot);
        gridView = findViewById(R.id.grid_layout_radio_button);
        BookAppointmentBtn = (Button) findViewById(R.id.BookAppointmentBtn);

        for (Map.Entry<String, Object> entry : availableSlot.entrySet()) {
            listAvailableSlot.add(String.valueOf(entry.getValue()));
        }


        adapter = new ArrayAdapter<String>(this, R.layout.list_item_single_choice, listAvailableSlot);
        gridView.setAdapter(adapter);

        BookAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    CreateFireStore(String.valueOf(listAvailableSlot.get(gridView.getCheckedItemPosition())),pkDate.getText().toString());
                }catch(Exception e){
                    Toast.makeText(BookingAppointmentActivity.this, "Select Time Please! ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    public void CreateFireStore(String Time,String Date){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Booking.put("Service", Service);
            Booking.put("Date", Date);
            Booking.put("Time", Time);
            Booking.put("UserId", user.getUid());
//            BookingDetails.put("BookingDetails",Booking);

            db.collection("Booking")
                    .add(Booking)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast toast = Toast.makeText(getApplicationContext(), "Booking Added Successfully", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent MainPage= new Intent(getApplicationContext(),MainPage.class);
                            startActivity(MainPage);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }else{
            Intent LoginPage= new Intent(getApplicationContext(),LoginPage.class);
            startActivity(LoginPage);
        }




    }

    private void removeRadioGroup(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
        listAvailableSlot.clear();
        NoTimeAva.setText("No Time Available");
    }



}
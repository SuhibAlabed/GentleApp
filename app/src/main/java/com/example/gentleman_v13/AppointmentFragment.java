package com.example.gentleman_v13;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.ListView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    ListView  listView;
    AppointmentAdapter adapter;

    Map<String, Object> Appointments                      = new HashMap<>();
    ArrayList<Map<String, Object>> listBookingAppointment = new ArrayList<Map<String, Object>>();



    private static final String TAG = "BookingAppointmentActivity";
    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment,
                container, false);

        TextView AskLogInText = (TextView) rootView.findViewById(R.id.AskLoginText);
        TextView  AskRegisterInText = (TextView) rootView.findViewById(R.id.AskRegister);
        TextView  OrText            = (TextView) rootView.findViewById(R.id.OrText);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            AskLogInText.setText("Login");
            OrText.setText("Or");
            AskRegisterInText.setText("Register");
            AskLogInText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent LoginPage= new Intent(getActivity(),LoginPage.class);
                    startActivity(LoginPage);
                }
            });

            AskRegisterInText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent LoginPage= new Intent(getActivity(),SignUpPage.class);
                    startActivity(LoginPage);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            GetBookingAppointment();
        }

        listView = (ListView) view.findViewById(R.id.list_layout_appointment);

    }

    private void GetBookingAppointment(){
        removeRadioGroup();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Booking")
                .whereEqualTo("UserId", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() ) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Appointments= document.getData();
//                                Log.d(TAG, document.getId() + " => " + document.getData());

                                createAppointmentsCard((Map<String, Object>) Appointments);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    private void createAppointmentsCard(Map<String, Object> appointments) {

        listBookingAppointment.add(appointments);

        adapter = new AppointmentAdapter(getActivity(), listBookingAppointment );

        listView.setAdapter(adapter);

    }

    private void removeRadioGroup(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
        listBookingAppointment.clear();
    }


}
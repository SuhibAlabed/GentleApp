package com.example.gentleman_v13;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppointmentAdapter extends ArrayAdapter<Map<String, Object>> {
    ArrayList<Map<String, Object>> AllData ;
    public AppointmentAdapter(Context context, ArrayList<Map<String, Object>> DateList) {
        super(context,  R.layout.activity_listview, DateList );
        AllData  = DateList;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview, parent, false);
        }

//        Log.d(TAG,"Test============>" + AllData.get(0).get("UserId"));

        TextView Service = (TextView) convertView.findViewById(R.id.services_text);
        TextView Date = (TextView) convertView.findViewById(R.id.date_text);
        TextView Time = (TextView) convertView.findViewById(R.id.time_text);
        // Populate the data into the template view using the data object
        Service.setText(String.valueOf(AllData.get(position).get("Service")));
        Date.setText(String.valueOf(AllData.get(position).get("Date")));
        Time.setText(String.valueOf(AllData.get(position).get("Time")));
        // Return the completed view to render on screen
        return convertView;
    }
}

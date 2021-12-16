package com.example.gentleman_v13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainPage extends Utilities implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private static final String TAG = "MainPage";
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigation;
    private boolean mSlideState=false;
    Map<String, Object> user = new HashMap<>();
    TextView UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mAuth = FirebaseAuth.getInstance();

        UserName = (TextView) findViewById(R.id.Username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        HomeFragment homeFragment = new HomeFragment();
        FavoritesFragment favoritesFragment = new FavoritesFragment();

        //Listener for Sidebar items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.LogoutBtn:
//                        sideBar();
                        signOut();
                        break;
                    case R.id.Settings:
                        break;
                }
                return true;
            }
        });
        //Listener for Bottom navigation
        bottomNavigation.setOnItemSelectedListener(new  NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.Home:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
//                        transaction.replace(R.id.flFragment, homeFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();
                        return true;
                    case R.id.Menu:
                        sideBar();
                      break;
                    case R.id.Save:

//                        transaction.replace(R.id.flFragment, favoritesFragment);
//                        transaction.addToBackStack(null);
//                        transaction.commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, favoritesFragment).commit();
                        return true;
                    default:
                        return false;
                }
                return false;
            }
        });

        drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this,
                drawerLayout,
                mToolbar,
                0,
                0){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState=false;//is Closed
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState=true;//is Opened
            }});

    }

//   When Start App
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getData(currentUser.getUid());
//            CheckAdmin(currentUser.getUid());
//            Toast.makeText(getApplicationContext(), currentUser.getUid(),
//                    Toast.LENGTH_SHORT).show();
        }else{
            UserName.setText("Guest");
        }

    }


//Sign Out Button
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent MainActivity= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(MainActivity);
    }

    public void getData(String UserId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("UserId", UserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                UserName.setText(document.getString("FullName"));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());

                        }
                    }
                });
    }



//    public void sideBar(){
//        ActionBarDrawerToggle actionBarDrawerToggle  = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @SuppressLint("RtlHardcoded")
    public void sideBar() {
        if(mSlideState){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }else{
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
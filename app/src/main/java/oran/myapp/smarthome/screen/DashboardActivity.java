package oran.myapp.smarthome.screen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import oran.myapp.smarthome.MainActivity;
import oran.myapp.smarthome.R;
import oran.myapp.smarthome.model.User;
import oran.myapp.smarthome.service.FirebaseService;
import oran.myapp.smarthome.service.broadcaster;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView home1,home2,home3,home4;
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance("https://esp32cam-110e7-default-rtdb.firebaseio.com/");
    private DatabaseReference usersRef= ROOT.getReference("users");


    private MainActivity inst = MainActivity.getInstance();
    private User userData = inst.getUserData();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        startService(new Intent(this, FirebaseService.class));
        init();

        if(!foregroundServiceRunning()) {
            Intent serviceIntent = new Intent(this, FirebaseService.class);
            startForegroundService(serviceIntent);
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.d("FCM Notify","Fetching FCM registration Fail ",task.getException());
                    return;

                }
                userData.setFcmToken(task.getResult());
                usersRef.child(userData.getUid()).child("fcmToken").setValue(task.getResult());
            }
        });

        home1.setOnClickListener(this);
        home2.setOnClickListener(this);
        home3.setOnClickListener(this);
        home4.setOnClickListener(this);
    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(FirebaseService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void init(){
        home1=findViewById(R.id.home1);
        home2=findViewById(R.id.home2);
        home3=findViewById(R.id.home3);
        home4=findViewById(R.id.home4);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(DashboardActivity.this,RoomsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, broadcaster.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}
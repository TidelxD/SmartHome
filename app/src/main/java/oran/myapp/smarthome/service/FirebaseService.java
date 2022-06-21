package oran.myapp.smarthome.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import oran.myapp.smarthome.R;
import oran.myapp.smarthome.screen.RoomDataActivity;
import oran.myapp.smarthome.screen.SensorsActivity;

public class FirebaseService extends Service {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase Root = FirebaseDatabase.getInstance("https://esp32-802ed-default-rtdb.firebaseio.com/");
        DatabaseReference usersRef = Root.getReference("users");
        DatabaseReference dataRef = Root.getReference();

        int LED1_state , LED2_state;




    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(FirebaseService.this, "data not found ! ", Toast.LENGTH_SHORT).show();
                    return;

                }
                 LED1_state = snapshot.child("PIR1").getValue(Integer.class);
                 LED2_state = snapshot.child("PIR2").getValue(Integer.class);

                if(LED2_state==1){
                    showNotification("Smart Home","Movment Detected InSide");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return super.startForegroundService(service);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(FirebaseService.this, "data not found ! ", Toast.LENGTH_SHORT).show();
                    return;

                }
                LED1_state = snapshot.child("PIR1").getValue(Integer.class);
                LED2_state = snapshot.child("PIR2").getValue(Integer.class);

                if(LED2_state==1){
                    showNotification("Smart Home","Movment Detected InSide");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        startForeground(1001,showNotificationForeGound("Smart Home"
        ,"Detection ..."));
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification(String title,
                                         String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, RoomDataActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.ic_home)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));
        } // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_home);
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());



    }

    public Notification showNotificationForeGound(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, RoomDataActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.ic_home)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));
        } // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_home);
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
//        NotificationManager notificationManager
//                = (NotificationManager) getSystemService(
//                Context.NOTIFICATION_SERVICE);
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel
//                    = new NotificationChannel(
//                    channel_id, "web_app",
//                    NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(
//                    notificationChannel);
//        }
//
//        notificationManager.notify(0, builder.build());

      return builder.build();

    }



    private RemoteViews getCustomDesign(String title,
                                        String message) {
        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.ic_home);
        return remoteViews;
    }






}

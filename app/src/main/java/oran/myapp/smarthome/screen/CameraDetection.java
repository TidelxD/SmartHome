package oran.myapp.smarthome.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import oran.myapp.smarthome.R;

public class CameraDetection extends AppCompatActivity {
        ImageView imageCaptured;

    // FIREBASE TOOLS
    FirebaseDatabase ROOT = FirebaseDatabase.getInstance("https://esp32cam-110e7-default-rtdb.firebaseio.com/");
    DatabaseReference dataRef = ROOT.getReference("data");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detection);

        imageCaptured = findViewById(R.id.imageCaptured);


        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) return ;


              String imgUrl =  snapshot.child("photoUrl").getValue(String.class);

                Picasso.get().load(imgUrl).into(imageCaptured);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CameraDetection.this, "cancled ! "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
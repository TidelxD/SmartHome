package oran.myapp.smarthome.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oran.myapp.smarthome.R;

public class SensorsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ImageView roomLED1 , LED1controller,LED2controller,roomLED2;
    private TextView LED1_status,LED2_status;

    // FIREBASE TOOLS
    FirebaseDatabase ROOT = FirebaseDatabase.getInstance("https://esp32-802ed-default-rtdb.firebaseio.com/");
    DatabaseReference dataRef = ROOT.getReference();

    private int LED1_state,LED2_state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        LED1_status=findViewById(R.id.LED1_status);
        LED2_status=findViewById(R.id.LED2_status);
        roomLED1=findViewById(R.id.roomLED1);
        LED1controller=findViewById(R.id.LED1controller);
        LED2controller=findViewById(R.id.LED2controller);
        roomLED2=findViewById(R.id.roomLED2);

        getData();

    }

    private void getData(){
        dataRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(SensorsActivity.this, "data not found ! ", Toast.LENGTH_SHORT).show();
                    return;

                }

                LED1_state = snapshot.child("PIR1").getValue(Integer.class);
                LED2_state = snapshot.child("PIR2").getValue(Integer.class);

                if(LED1_state==0){

                    roomLED1.setImageDrawable(getDrawable(R.drawable.ic_light_off));
                    LED1_status.setText("Nothing Detected out side");
                    LED1_status.setTextColor(Color.parseColor("#000000"));
                    LED1controller.setImageDrawable(getDrawable(R.drawable.ic_officier));
                    dataRef.child("LED1").setValue(0);

                }
                else {
                    roomLED1.setImageDrawable(getDrawable(R.drawable.ic_light_on));
                    LED1_status.setText("Movment Detected out side");
                    LED1_status.setTextColor(Color.parseColor("#00e6c3"));
                    LED1controller.setImageDrawable(getDrawable(R.drawable.ic_officier_on));
                    dataRef.child("LED1").setValue(1);


                }

                if(LED2_state==0){

                    roomLED2.setImageDrawable(getDrawable(R.drawable.ic_light_off));
                    LED2_status.setText("Nothing Detected in side");
                    LED2_status.setTextColor(Color.parseColor("#000000"));

                    LED2controller.setImageDrawable(getDrawable(R.drawable.ic_officier));
                    dataRef.child("LED2").setValue(0);

                }
                else {
                    roomLED2.setImageDrawable(getDrawable(R.drawable.ic_light_on));
                    LED2_status.setText("Movment Detected in side");
                    LED2_status.setTextColor(Color.parseColor("#00e6c3"));
                    LED2controller.setImageDrawable(getDrawable(R.drawable.ic_officier_on));
                    dataRef.child("LED2").setValue(1);
                    mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorsActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }
}
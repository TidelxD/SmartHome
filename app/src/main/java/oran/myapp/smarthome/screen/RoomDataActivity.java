package oran.myapp.smarthome.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oran.myapp.smarthome.R;

public class RoomDataActivity extends AppCompatActivity {


    private CardView LED3card,LED3Button,othersensors;
    private ImageView roomLED3,LED3controller;
    private TextView LED3_status;

    // FIREBASE TOOLS
    FirebaseDatabase ROOT = FirebaseDatabase.getInstance("https://esp32-802ed-default-rtdb.firebaseio.com/");
    DatabaseReference dataRef = ROOT.getReference();

    private int LED3_state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_data);

        init();

        getData();


        LED3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int newLED3_State ;
                if (LED3_state==0) newLED3_State=1; else newLED3_State=0;

                dataRef.child("LED3").setValue(newLED3_State);
            }
        });

        othersensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomDataActivity.this,SensorsActivity.class);
                startActivity(intent);
            }
        });



    }



    private void init(){

        LED3card=findViewById(R.id.LED3card);
        LED3Button=findViewById(R.id.LED3Button);
        roomLED3=findViewById(R.id.roomLED3);
        LED3controller=findViewById(R.id.LED3controller);
        LED3_status=findViewById(R.id.LED3_status);
        othersensors=findViewById(R.id.othersensors);


    }

    private void getData(){
        dataRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(RoomDataActivity.this, "data not found ! ", Toast.LENGTH_SHORT).show();
                    return;

                }

                LED3_state = snapshot.child("LED3").getValue(Integer.class);

                if(LED3_state==0){

                    roomLED3.setImageDrawable(getDrawable(R.drawable.ic_light_off));
                    LED3_status.setText("ROOM LED IS OFF");
                    LED3_status.setTextColor(Color.parseColor("#000000"));

                    LED3controller.setImageDrawable(getDrawable(R.drawable.ic_turn_on));

                }else {
                    roomLED3.setImageDrawable(getDrawable(R.drawable.ic_light_on));
                    LED3_status.setText("ROOM LED IS ON");
                    LED3_status.setTextColor(Color.parseColor("#00e6c3"));
                    LED3controller.setImageDrawable(getDrawable(R.drawable.ic_turn_off));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RoomDataActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }





}
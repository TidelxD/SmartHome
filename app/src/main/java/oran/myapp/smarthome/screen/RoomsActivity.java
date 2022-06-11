package oran.myapp.smarthome.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import oran.myapp.smarthome.R;

public class RoomsActivity extends AppCompatActivity implements View.OnClickListener {


    private CardView room1, room2, room3, room4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        init();

        room1.setOnClickListener(this);
        room2.setOnClickListener(this);
        room3.setOnClickListener(this);
        room4.setOnClickListener(this);

    }


    private void init() {
        room1 = findViewById(R.id.room1);
        room2 = findViewById(R.id.room2);
        room3 = findViewById(R.id.room3);
        room4 = findViewById(R.id.room4);
    }


    @Override
    public void onClick(View view) {
              Intent intent = new Intent(RoomsActivity.this,RoomDataActivity.class);
              startActivity(intent);
    }
}
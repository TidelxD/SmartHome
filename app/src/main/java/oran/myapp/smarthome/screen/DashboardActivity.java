package oran.myapp.smarthome.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import oran.myapp.smarthome.R;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView home1,home2,home3,home4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();

        home1.setOnClickListener(this);
        home2.setOnClickListener(this);
        home3.setOnClickListener(this);
        home4.setOnClickListener(this);
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
}
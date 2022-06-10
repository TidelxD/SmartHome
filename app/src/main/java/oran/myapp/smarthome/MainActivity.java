package oran.myapp.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oran.myapp.smarthome.model.User;
import oran.myapp.smarthome.screen.DashboardActivity;
import oran.myapp.smarthome.screen.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef= ROOT.getReference("users");
    private static MainActivity inst ;

    public MainActivity (){
        inst = this;
    }

    public static MainActivity getInstance(){

        return inst;
    }

    private User userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(mAuth.getCurrentUser()!=null){
            usersRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()) {
                        Toast.makeText(MainActivity.this, "user Not Exist", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    userData = snapshot.getValue(User.class);
                    Toast.makeText(MainActivity.this,"login Successfully ! ",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            },1500);

        }

    }


    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }
}
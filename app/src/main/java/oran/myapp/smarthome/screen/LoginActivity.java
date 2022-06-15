package oran.myapp.smarthome.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oran.myapp.smarthome.MainActivity;
import oran.myapp.smarthome.R;
import oran.myapp.smarthome.model.User;

public class LoginActivity extends AppCompatActivity {

    // DATABASE TOOLS
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance("https://esp32-802ed-default-rtdb.firebaseio.com/");
    private DatabaseReference usersRef= ROOT.getReference("users");

    // VIEWS TOOLS
    private LinearLayout sign_in,sign_up ;
    private EditText emailEdit, passwordEdit;
    private ProgressDialog progressDialog;
    private MainActivity inst = MainActivity.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init ();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }



    private void init (){

        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ... ");
        progressDialog.setCancelable(false);
    }
    private void login(){
        String email = emailEdit.getText().toString();

        String password = passwordEdit.getText().toString();

        if (email.isEmpty()){
            emailEdit.setError("Required ! ");
            return;
        }

        if (password.isEmpty()){
            passwordEdit.setError("Required ! ");
            return;
        }
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"auth err: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }

                usersRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if(!snapshot.exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "user Not Exist", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        User helper  = snapshot.getValue(User.class);
                        inst.setUserData(helper);
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"login Successfully ! ",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
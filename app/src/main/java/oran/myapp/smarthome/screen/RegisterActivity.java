package oran.myapp.smarthome.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import oran.myapp.smarthome.MainActivity;
import oran.myapp.smarthome.R;
import oran.myapp.smarthome.model.User;

public class RegisterActivity extends AppCompatActivity {

      // DATABASE TOOLS
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase ROOT = FirebaseDatabase.getInstance("https://esp32cam-110e7-default-rtdb.firebaseio.com/");
    private DatabaseReference usersRef= ROOT.getReference("users");

    // VIEWS TOOLS
    private LinearLayout sign_in,sign_up ;
    private EditText emailEdit, nameEdit, phoneEdit, passwordEdit;
    private ProgressDialog progressDialog;
    private MainActivity inst = MainActivity.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R    .layout.activity_register);

        // Initialization
        init ();

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

    }


    private void init (){
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
        emailEdit = findViewById(R.id.email);
        nameEdit = findViewById(R.id.fullname);
        phoneEdit = findViewById(R.id.phone);
        passwordEdit = findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ... ");
        progressDialog.setCancelable(false);
    }


    private void Register(){
        String email = emailEdit.getText().toString();
        String full_name = nameEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (email.isEmpty()){
            emailEdit.setError("Required ! ");
            return;
        }
        if (full_name.isEmpty()){
            phoneEdit.setError("Required ! ");
            return;
        }
        if (phone.isEmpty()){
            emailEdit.setError("Required ! ");
            return;
        }
        if (password.isEmpty()){
            passwordEdit.setError("Required ! ");
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                  if(!task.isSuccessful()){
                      progressDialog.dismiss();
                      Toast.makeText(RegisterActivity.this,"auth err: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                      return;
                  }

                  User helper = new User(mAuth.getCurrentUser().getUid(),full_name,phone,email,password);

                  usersRef.child(helper.getUid()).setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if(!task.isSuccessful()){
                              progressDialog.dismiss();
                              Toast.makeText(RegisterActivity.this,"user err: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                              return;
                          }
                          progressDialog.dismiss();
                          inst.setUserData(helper);

                          Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                          intent.putExtra("PhoneNo",helper.getPhone());
                          startActivity(intent);
                          finish();
                          Toast.makeText(RegisterActivity.this,"Register Successfully ! ",Toast.LENGTH_SHORT).show();
                      }
                  });
            }
        });


    }

}
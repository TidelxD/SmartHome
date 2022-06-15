package oran.myapp.smarthome.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import oran.myapp.smarthome.R;

public class VerificationOtp extends AppCompatActivity {

    private String verifyCodeBySystem,phoneNo;
    private LinearLayout sign_in;
    private EditText phnNNo;
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference DBrefrence = rootNode.getReference("users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_otp);

        phnNNo = findViewById(R.id.editText);
        sign_in = findViewById(R.id.sign_in);

        phoneNo=getIntent().getStringExtra("PhoneNo");

        sendVerificationCodeToUser(phoneNo);
    }


    // Operation pour envoyer le code OTP
    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+213"+phoneNo)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    // fonction pour recuperer le code OTP
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // Operation pour recupérer si la carte sim est dant un autre telephone
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verifyCodeBySystem = s;
            Log.d("My Activity", "the code when another device  is : " + verifyCodeBySystem);

        }
        //Operation pour recpérer si la carte sim est dant le mem telephone

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {


                Log.d("My Activity", "the when the same device code is : " + code);
                verifycode(code);

            }
        }

        // Operation si'l ya un probleme
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerificationOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // Operation pour verify le code OTP réel est le code envoye
    private void verifycode(String code) {

        // compare between code written and the code that sent by System
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCodeBySystem, code);
        // use signInWithCrendential to set the user on firebase authentication

        signInTheUser(credential);

    }
    // fonction poue connecter si le code est bien verifier
    private void signInTheUser(PhoneAuthCredential credential) {

        // FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // y7ot data fel firebase ba3d ma khdemha kamel bel ID
        // fonction pour ajouter les données dan't le firebase est connecter
        mAuth.signInWithCredential(credential).addOnCompleteListener(VerificationOtp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Opeation pour verifier si la tache est succés
                if (task.isSuccessful()) {
                    String authID= mAuth.getCurrentUser().getUid();


                } else {
                    Toast.makeText(VerificationOtp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



}
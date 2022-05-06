package com.example.biztositas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();


    EditText userFirstName;
    EditText userLastName;
    EditText userEmail;
    EditText userPassword;
    EditText userPasswordAgain;
    EditText userPhone;

    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private CollectionReference mUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        userFirstName = findViewById(R.id.firstNameEditText);
        userLastName = findViewById(R.id.lastNameEditText);
        userEmail = findViewById(R.id.userEmailEditText);
        userPassword = findViewById(R.id.userPasswordEditText);
        userPasswordAgain = findViewById(R.id.passwordAgain);
        userPhone = findViewById(R.id.phoneEditText);

        mAuth = FirebaseAuth.getInstance();

        mFirestore = FirebaseFirestore.getInstance();
        mUsers = mFirestore.collection("Users");

    }

    public void register(View view) {
        // lekérem az texteket amiket megadott
        String email = userEmail.getText().toString();
        String passwordStr = userPassword.getText().toString();
        String passwordConfirm = userPasswordAgain.getText().toString();
        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String phoneNumber = userPhone.getText().toString();

        // a két jelszó ellenőrzése
        if(!passwordConfirm.equals(passwordStr)){
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            return;
        }

        // autentikálom firebase Auth-al
        mAuth.createUserWithEmailAndPassword(email,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeresen létrejött a User");
                   startCars();
                }else{
                    Log.d(LOG_TAG, "Nem jött létre a User");
                    Toast.makeText(RegisterActivity.this,"Nem jött létre a User"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // el kell mentenem az egyéb adatokat FIRESTORE-ba
        User user = new User(firstName, lastName, email,phoneNumber);
        mUsers.add(user);


    }

    public void cancel(View view) {
    finish();
    }

    private void startCars(){
        Intent intent = new Intent(this, CarsActivity.class);
        startActivity(intent);
    }


}
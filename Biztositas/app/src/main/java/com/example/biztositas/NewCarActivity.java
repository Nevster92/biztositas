package com.example.biztositas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class NewCarActivity extends AppCompatActivity {

    EditText licenceNumberEditText;
    EditText trafficLicenceEditText;
    EditText chassisNumberEditText;
    EditText carTypeEditText;
    EditText licenceExpireEditText;
    EditText bonusEditText;

    private FirebaseFirestore mFirestore;
    private CollectionReference mCar;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car_layout);

        licenceNumberEditText = findViewById(R.id.licenceNumber);
        trafficLicenceEditText = findViewById(R.id.trafficLicence);
        chassisNumberEditText = findViewById(R.id.chassisNumber);
        carTypeEditText = findViewById(R.id.carType);
        licenceExpireEditText = findViewById(R.id.licenceExpire);
        bonusEditText = findViewById(R.id.bonus);

        mFirestore = FirebaseFirestore.getInstance();
        mCar = mFirestore.collection("Cars");
        mAuth = FirebaseAuth.getInstance();



    }


    public void cancel(View view) {finish();}

    public void addNewCar(View view) {
        String owner = mAuth.getCurrentUser().getEmail();
        String licenceNumber = licenceNumberEditText.getText().toString();
        String trafficLicence = trafficLicenceEditText.getText().toString();
        String chassisNumber = chassisNumberEditText.getText().toString();
        String carType = carTypeEditText.getText().toString();
        String licenceExpire = licenceExpireEditText.getText().toString();
        String bonusString = bonusEditText.getText().toString();
        int bonus;

        if(licenceNumber.equals("")){
            showDialog("A rendszám kötelező.");
        }else if (bonusString.equals("")){
            showDialog("A bónusz megadása kötelező.");
        }else {
            bonus = Integer.parseInt(bonusString);
            // ellenőrzi hogy van e ilyen rendszámú autóü a rendszerben
            mFirestore.collection("Cars")
                    .whereEqualTo("licenceNumber", licenceNumber)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean talalt = false;
                                for (DocumentSnapshot document : task.getResult()) {
                                    talalt = true;
                                }
                                if (talalt) {
                                    showDialog("Van ilyen rendszámú autó a rendszerben.");
                                } else {
                                    Car newCar = new Car(
                                            licenceNumber,
                                            trafficLicence,
                                            chassisNumber,
                                            carType,
                                            licenceExpire,
                                            bonus,
                                            owner);
                                    mCar.add(newCar);  // hozzáadja az adatbázishoz
                                    backToCars();
                                }
                            }
                        }
                    });
        }
    }

    private void backToCars(){
        Intent intent = new Intent(this, CarsActivity.class);
        startActivity(intent);
    }

    public void showDialog(String message){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.error_dialog);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        final TextView title = dialog.findViewById(R.id.errorDialogText);
        title.setText(message);
        Button okButton = dialog.findViewById(R.id.errorDialogButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
package com.example.biztositas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CarModifyActivity extends AppCompatActivity {

        EditText trafficLicenceEditText;
        EditText chassisNumberEditText;
        EditText carTypeEditText;
        EditText licenceExpireEditText;
        EditText bonusEditText;

        private FirebaseFirestore mFirestore;
        private CollectionReference mCar;

        private String selectedCarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_car);

        selectedCarId= getIntent().getStringExtra("selectedCarId");


        trafficLicenceEditText = findViewById(R.id.trafficLicence);
        chassisNumberEditText = findViewById(R.id.chassisNumber);
        carTypeEditText = findViewById(R.id.carType);
        licenceExpireEditText = findViewById(R.id.licenceExpire);
        bonusEditText = findViewById(R.id.bonus);

        mFirestore = FirebaseFirestore.getInstance();
        mCar = mFirestore.collection("Cars");

        initializeData();
    }

    public void cancel(View view) {finish();}


    public void initializeData(){
        mCar.document(selectedCarId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    trafficLicenceEditText.setText(task.getResult().getString("trafficLicenceNumber"));
                    chassisNumberEditText.setText(task.getResult().getString("chassisNumber"));
                    carTypeEditText.setText(task.getResult().getString("carType"));
                    licenceExpireEditText.setText(task.getResult().getString("licenceExpire"));
                    String bonus = String.valueOf(task.getResult().getLong("bonus").intValue()) ;
                    bonusEditText.setText(bonus);

                }
            }
        });
    }

    public void modifyCar(View view){
        // valamelyik field üres
        if(licenceExpireEditText.getText().toString().equals("")
            || trafficLicenceEditText.getText().toString().equals("")
            || chassisNumberEditText.getText().toString().equals("")
            || carTypeEditText.getText().toString().equals("")
            || bonusEditText.getText().toString().equals(""))
            {
                showDialog(this, "Mindegyik mező kitöltése kötelező!");
            }
        else{
            String bonusString = bonusEditText.getText().toString();
            int bonus = Integer.parseInt(bonusString);

            mFirestore.collection("Cars").document(selectedCarId).update("licenceExpire",licenceExpireEditText.getText().toString());
            mFirestore.collection("Cars").document(selectedCarId).update("trafficLicenceNumber",trafficLicenceEditText.getText().toString());
            mFirestore.collection("Cars").document(selectedCarId).update("chassisNumber",chassisNumberEditText.getText().toString());
            mFirestore.collection("Cars").document(selectedCarId).update("carType",carTypeEditText.getText().toString());
            mFirestore.collection("Cars").document(selectedCarId).update("bonus",bonus);
            Intent intent = new Intent(this, CarsActivity.class);
            startActivity(intent);
        }

    }

    public void showDialog(Context context, String message){
        final Dialog dialog = new Dialog(context);
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
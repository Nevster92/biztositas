package com.example.biztositas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CarsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Car> mCarList;
    private CarsAdapter mAdapter;

    private int gridNumber = 1;

    private NotificationHandler mNotificationHandler;
    private AlarmManager mAlarmManager;

    private FirebaseFirestore mFirestore;
    private CollectionReference mCars;   //Firestore kollekciójának hivatkozásához
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cars_layout);


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mCarList = new ArrayList<>();
        mAdapter = new CarsAdapter(this, mCarList);
        mRecyclerView.setAdapter(mAdapter);

        mNotificationHandler = new NotificationHandler(this);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mFirestore = FirebaseFirestore.getInstance();
        mCars = mFirestore.collection("Cars");
        mAuth = FirebaseAuth.getInstance();

        setAlarmManager();

        initializeData();
    }

    private void initializeData() {

        // mCarList.add(egyesével)
        String currentUserEmail = mAuth.getCurrentUser().getEmail();

        mCars.limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                if(document.get("owner").toString().equals(currentUserEmail)){
                    Car car = new Car();
                    car.setLicenceNumber(document.get("licenceNumber").toString());
                    car.setCarType(document.get("carType").toString());
                    int bonus = document.getLong("bonus").intValue();
                    car.setBonus(bonus);
                    car.setLicenceExpire(document.get("licenceExpire").toString());
                    mCarList.add(car);
                }
            }
            mAdapter.notifyDataSetChanged();
        });


    }


    public void onNewCar(View view) {
        // TODO Kellene ide valamilyen animáció
        Intent intent = new Intent(this, NewCarActivity.class);
        startActivity(intent);
    }

    public void addNewCarInsurance(Car car){

        mCars.get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        if(document.get("licenceNumber").equals(car.getLicenceNumber())){
                            String CARID = document.getId();
                            Intent intent = new Intent(this,AddInsuranceActivity.class);
                            intent.putExtra("selectedCarId", document.getId());
                            startActivity(intent);
                        }
                    }
                });

    }

    public void deleteCar(Car car){
        mCars.get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        if(document.get("licenceNumber").equals(car.getLicenceNumber())){
                            String deletedCarId = document.getId();
                            mCars.document(deletedCarId)
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                finish();
                                                startActivity(getIntent());
                                        }
                                    });

                        }
                    }
                });
    }

    public void modifyCar(Car car){
        mCars.get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                        if(document.get("licenceNumber").equals(car.getLicenceNumber())){
                            String CARID = document.getId();
                            Intent intent = new Intent(this, CarModifyActivity.class);
                            intent.putExtra("selectedCarId", document.getId());
                            startActivity(intent);
                        }
                    }
                });
    }

    private void setAlarmManager(){
        long repeatIntervall = 60*100;
        long triggerTime = SystemClock.elapsedRealtime() + repeatIntervall;

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatIntervall,
                pendingIntent
        );

    }

}
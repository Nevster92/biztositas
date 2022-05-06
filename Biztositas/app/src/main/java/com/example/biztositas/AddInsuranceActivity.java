package com.example.biztositas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class AddInsuranceActivity extends AppCompatActivity   {

    private RecyclerView mRecyclerView;
    private ArrayList<Insurance> mInsuranceList;
    private InsuranceAdapter mAdapter;

    private int gridNumber = 1;

    private FirebaseFirestore mFirestore;
    private CollectionReference mInsurances;



    private String selectedCarId;
    private String selectedInsuranceId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_insurance);

        selectedCarId= getIntent().getStringExtra("selectedCarId");


        context = this;
        mRecyclerView = findViewById(R.id.InsuranceRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mInsuranceList = new ArrayList<>();
        mAdapter = new InsuranceAdapter(this, mInsuranceList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mInsurances = mFirestore.collection("Insurances");


        initializeData();
    }






    private void initializeData() {
        mInsurances.limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                   Insurance insurance = new Insurance();
                insurance.setCompany(document.get("company").toString());
                insurance.setPeriod(document.get("period").toString());
                    int price = document.getLong("price").intValue();
                    insurance.setPrice(price);
                    mInsuranceList.add(insurance);
            }

            mAdapter.notifyDataSetChanged();
        });


    }




    public void addInsurance(Insurance insurance) {

        mFirestore.collection("Insurances")
                .whereEqualTo("company", insurance.getCompany())
                .whereEqualTo("period", insurance.getPeriod())
                .whereEqualTo("price", insurance.getPrice())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                selectedInsuranceId = document.getId();  // ez adja visza az id-t

                                mFirestore.collection("Cars").document(selectedCarId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.getResult().getString("insuranceId") != null ){
                                                showDialog(context, "Ennek az autónak már van biztosítása");
                                            }else{
                                                mFirestore.collection("Cars").document(selectedCarId).update("insuranceId",selectedInsuranceId);
                                                Intent intent = new Intent(context, CarsActivity.class);
                                                startActivity(intent);
                                            }
                                    }
                                });


                            }
                        }
                    }

                });



    }


    public void onBackToCars(View view) {
        Intent intent = new Intent(this, CarsActivity.class);
        startActivity(intent);

    }



    public void showDialog(Context context,String message){
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



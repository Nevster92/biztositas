package com.example.biztositas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.ViewHolder> {

    private ArrayList<Insurance> mInsuranceData;
    private ArrayList<Insurance> mInsuranceDataAll;
    private Context mContext;

    public InsuranceAdapter(Context context, ArrayList<Insurance> insurancesData) {
    this.mInsuranceData = insurancesData;
    this.mInsuranceDataAll = insurancesData;
    this.mContext = context;

    }


    @NonNull
    @Override
    public InsuranceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_insurance, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InsuranceAdapter.ViewHolder holder, int position) {
        Insurance actualInsurance = mInsuranceDataAll.get(position);
        holder.bindTo(actualInsurance);
    }

    @Override
    public int getItemCount() {
        return mInsuranceData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView companyText;
        private TextView periodText;
        private TextView priceText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyText = itemView.findViewById(R.id.company);
            periodText = itemView.findViewById(R.id.period);
            priceText = itemView.findViewById(R.id.price);
        }

        public void bindTo(Insurance actualInsurance) {
            // itt kötöm össze az osztályt és a view-t
            companyText.setText(actualInsurance.getCompany());
            periodText.setText(actualInsurance.getPeriod());
            priceText.setText(String.valueOf(actualInsurance.getPrice()));    // nehogy hibát dobjon mer a bonus INT


            itemView.findViewById(R.id.insuranceListItem).setOnClickListener(view -> ((AddInsuranceActivity)mContext).addInsurance(actualInsurance));
        }
    }



}

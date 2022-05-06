package com.example.biztositas;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {
    private ArrayList<Car> mCarsData;
    private ArrayList<Car> mCarsDataAll;
    private Context mContext;



    CarsAdapter(Context context, ArrayList<Car> carsData){
        this.mCarsData = carsData;
        this.mCarsDataAll = carsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ezzel kötjük össze a layoutot az adapterhez.
        ViewHolder vHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false));
        return vHolder;

    }

    @Override
    public void onBindViewHolder(CarsAdapter.ViewHolder holder, int position) {
        Car actualCar = mCarsDataAll.get(position);

        holder.bindTo(actualCar);

    }

    @Override
    public int getItemCount() {
        return mCarsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView licenceNumberText;
        private TextView carTypeText;
        private TextView bonusText;
        private TextView expireText;
        private int selectedCarPosition;

        public int getSelectedCarPosition() {
            return selectedCarPosition;
        }

        public void setSelectedCarPosition(int selectedCarPosition) {
            this.selectedCarPosition = selectedCarPosition;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            licenceNumberText = itemView.findViewById(R.id.licenceNumberList);
            carTypeText = itemView.findViewById(R.id.carTypeList);
            bonusText = itemView.findViewById(R.id.bonusList);
            expireText = itemView.findViewById(R.id.licenceExpireList);



        }

        public void bindTo(Car actualCar) {
            // itt kötöm össze az osztályt és a view-t
            licenceNumberText.setText(actualCar.getLicenceNumber());
            carTypeText.setText(actualCar.getCarType());
            bonusText.setText(String.valueOf(actualCar.getBonus()));    // nehogy hibát dobjon mer a bonus INT
            expireText.setText(actualCar.getLicenceExpire());


            itemView.findViewById(R.id.carRow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu((CarsActivity)mContext, view);
                    popupMenu.getMenuInflater().inflate(R.menu.car_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.add_new_insurance:
                                    ((CarsActivity)mContext).addNewCarInsurance(actualCar);
                                    return true;
                                case R.id.delete_car:
                                    ((CarsActivity)mContext).deleteCar(actualCar);
                                    return true;
                                case R.id.modify_car:
                                    ((CarsActivity)mContext).modifyCar(actualCar);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

        }
    }


}


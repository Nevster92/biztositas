package com.example.biztositas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       new NotificationHandler(context).send("Nézz rá a biztosításaidra.");
    }
}
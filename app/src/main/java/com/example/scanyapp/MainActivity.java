package com.example.scanyapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.example.scanyapp.retrofit.RetrofitScanner;
import com.example.scanyapp.retrofit.Static;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    Intent intent;
    NotificationCompat.Builder builder;
    Intent notificationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        createNotificationChannel();
        notificationIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        PendingIntent result = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(this, "666")
                .setSmallIcon(R.drawable.bat_outlines)
                .setContentTitle("Scanner finished")
                .setContentText("Devices can be updated now")
                .setContentIntent(result)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        intent = new Intent(this, RetrofitScanner.class);
        if(SPStaticUtils.getString(Static.KeyServer).isEmpty() && SPStaticUtils.getString(Static.KeyToken).isEmpty()) {
            FragmentUtils.replace(getSupportFragmentManager(), new LoginFragment(), R.id.fragment);
        }
        else
        {
            if(checkConnection())
                RetrofitScanner.retrofitAuth();
            else {
                FragmentUtils.replace(getSupportFragmentManager(), new HomeFragment(), R.id.fragment);
                Toast.makeText(this, "No Internet Connection, only showing saved entries", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "Scany notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("666", name, importance);
            channel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Subscribe
    public void onScannerEvent(ScannerEvent event) {
        if (event.key.equals(Static.KeyLogin)) { FragmentUtils.replace(getSupportFragmentManager(), new LoginFragment(), R.id.fragment); }
        if (event.key.equals(Static.KeyStart)) {
            FragmentUtils.replace(getSupportFragmentManager(), new HomeFragment(), R.id.fragment);
            startService(intent);
        }
        if (event.key.equals(Static.KeyMsg)) { Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show(); }
        if (event.key.equals(Static.KeyFailure)){ stopService(intent); }
        if (event.key.equals(Static.KeyUpdate)) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(420, builder.build());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}

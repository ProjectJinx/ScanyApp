package com.example.scanyapp.retrofit;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPStaticUtils;
import com.example.scanyapp.ScannerEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitScanner extends Service {

    Handler handler;
    TimerTask timerTask;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(RetrofitScanner::retrofitGet);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,600000);
        Log.d("Scanner::", "I'm here ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("RetrofitScanner", "onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void retrofitAuth()
    {
        GetData service = ClientInstance.getRetrofitInstance().create(GetData.class);
        Call<AuthResponse> call = service.getToken(SPStaticUtils.getString(Static.KeyPassword));
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("GetToken::onResponse", response.toString());
                Log.d("GetToken::onResponse", response.body().token);
                if(response.body().token.equals("None")) {
                    EventBus.getDefault().post(new ScannerEvent(Static.KeyLogin));
                    EventBus.getDefault().post(new ScannerEvent(Static.KeyFailure));
                }
                else {
                    SPStaticUtils.put(Static.KeyToken, response.body().token);
                    //EventBus.getDefault().post(new ScannerEvent(Static.KeyMsg, "Successfully obtained token"));
                    EventBus.getDefault().post(new ScannerEvent(Static.KeyStart));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().post(new ScannerEvent(Static.KeyFailure));
                EventBus.getDefault().post(new ScannerEvent(Static.KeyMsg, t.getMessage()));
                EventBus.getDefault().post(new ScannerEvent(Static.KeyLogin));
            }
        });
    }

    private static void retrofitGet()
    {
        GetData service = ClientInstance.getRetrofitInstance().create(GetData.class);
        Call<List<Device>> call = service.getAllDevices(SPStaticUtils.getString(Static.KeyToken));
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                Log.d("GAD::onResponse", response.toString());
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {
                t.printStackTrace();
                retrofitAuth();
                SPStaticUtils.remove(Static.KeyToken);
                EventBus.getDefault().post(new ScannerEvent(Static.KeyMsg, t.getMessage()));
            }
        });
    }
    private static void generateDataList(List<Device> devices) {
        Log.d("DataList::", String.valueOf(devices.size()));
        Paper.book().write("devices", devices);
        EventBus.getDefault().post(new ScannerEvent(Static.KeyUpdate));
    }
}

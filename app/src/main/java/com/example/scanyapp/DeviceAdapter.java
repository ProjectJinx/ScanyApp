package com.example.scanyapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanyapp.retrofit.Device;
import com.example.scanyapp.retrofit.Static;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> devices;

    public DeviceAdapter(){
        updateDevices();
    }

    public void updateDevices()
    {
        devices = Paper.book().read("devices");
        if(devices == null)
        {
            devices=new ArrayList<>();

        }
        Log.d("UpdateDevices::", String.valueOf(devices.size()));
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        private Device dev;
        DeviceViewHolder(View V) {
            super(V);
            ButterKnife.bind(this, V);
        }

        @BindView(R.id.Img)
        ImageView img;
        @BindView(R.id.IP)
        TextView ip;
        @BindView(R.id.MAC)
        TextView mac;

        @OnClick(R.id.Fav)
        void FavClicked()
        {
            EventBus.getDefault().post(new ScannerEvent(Static.KeyMsg, dev.getMac() + "is now a favourite"));
            dev.setFavd();
            img.setImageResource(R.drawable.laptop_heart);
        }

        void setDev(Device d)
        {
            this.dev=d;
            ip.setText(dev.getIp());
            mac.setText(dev.getMac());
            img.setImageResource(R.drawable.laptop);

        }
    }
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder VH, int pos){
        VH.setDev(devices.get(pos));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

}
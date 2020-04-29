package com.example.scanyapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanyapp.retrofit.Static;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @BindView(R.id.Rv) RecyclerView Devices;
    private ProgressDialog prog;
    private DeviceAdapter adapter;

    @BindView(R.id.txtSync)
    TextView txtsync;

    @OnClick(R.id.btSync)
    void Sync(){
        adapter.updateDevices();
        txtsync.setText("All up to date!");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container,  false);
        ButterKnife.bind(this,v);
        prog = new ProgressDialog(getContext());
        prog.setMessage("Loading...");
        prog.show();
        Log.d("Home Fragment::", "I'm trying: ");
        adapter = new DeviceAdapter();
        Devices.setLayoutManager(new LinearLayoutManager(getContext()));
        Devices.setAdapter(adapter);
        Sync();
        prog.dismiss();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onScannerEvent(ScannerEvent event) {
        if (event.key.equals(Static.KeyUpdate)) {
            txtsync.setText("Devices can be updated!");
        }
    }
}

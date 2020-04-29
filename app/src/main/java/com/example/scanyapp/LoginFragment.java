package com.example.scanyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.SPStaticUtils;
import com.example.scanyapp.retrofit.RetrofitScanner;
import com.example.scanyapp.retrofit.Static;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blankj.utilcode.util.EncryptUtils.encryptSHA256ToString;

public class LoginFragment extends Fragment {
    @BindView(R.id.txserver) EditText Server;
    @BindView(R.id.password) EditText Password;

    @OnClick(R.id.btnconnect)
    void Connect()
    {
        String srv = Server.getText().toString();
        String pass = Password.getText().toString();
        if(!srv.isEmpty() && !pass.isEmpty())
        {
            pass = encryptSHA256ToString(pass);
            Log.d("LoginFragment", pass);
            SPStaticUtils.put(Static.KeyServer, srv);
            SPStaticUtils.put(Static.KeyPassword, pass);
            RetrofitScanner.retrofitAuth();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        if(!SPStaticUtils.getString("Server").isEmpty())
            Server.setText(SPStaticUtils.getString("Server"));
        super.onResume();
    }
}


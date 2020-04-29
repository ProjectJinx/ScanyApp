package com.example.scanyapp.retrofit;

public class Device {
    private String ip;
    private String mac;
    private String firstconnect;
    private String lastconnect;

    private boolean favd;

    public Device(String ip, String mac, String firstconnect, String lastconnect)
    {
        this.ip=ip;
        this.mac=mac;
        this.firstconnect=firstconnect;
        this.lastconnect=lastconnect;
        this.favd=false;
    }

    public String getIp()
    {
        return ip;
    }

    public String getMac()
    {
        return mac;
    }

    public String getFirstconnect()
    {
        return firstconnect;
    }

    public String getLastconnect()
    {
        return lastconnect;
    }

    public void setLastconnect(String conn)
    {
        lastconnect=conn;
    }

    public Boolean getFavd()
    {
        return favd;
    }

    public void setFavd()
    {
        favd= !favd;
    }

}




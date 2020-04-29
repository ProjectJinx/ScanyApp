package com.example.scanyapp;

public class ScannerEvent {
    public String message;
    public String key;

    public ScannerEvent(String Message)
    {
        this.key=Message;
    }

    public ScannerEvent(String Key, String Message)
    {
        this.message = Message;
        this.key = Key;
    }
}

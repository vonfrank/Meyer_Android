package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Von Frank on 11-05-2016.
 */
public class BluetoothClientController extends Thread{

    private BluetoothAdapter mBluetoothAdapter;
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static final UUID RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothClientController(BluetoothDevice device, BluetoothAdapter bluetoothAdapter){
        mBluetoothAdapter = bluetoothAdapter;
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createInsecureRfcommSocketToServiceRecord(RFCOMM_UUID);
        } catch (IOException e){}
        mmSocket = tmp;
    }

    public void run(){
        mBluetoothAdapter.cancelDiscovery();

        try {
            mmSocket.connect();
        } catch(IOException connectException){
            System.out.println("Unable to connect to server");
            try{
                mmSocket.close();
            } catch (IOException closeException){}
            return;
        }
        System.out.println("Connected to server");
        //Her skal connection bruges!!!!
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}

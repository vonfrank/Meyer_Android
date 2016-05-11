package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Von Frank on 10-05-2016.
 */
public class BluetoothServerController extends Thread{

    private BluetoothAdapter mBluetoothAdapter;
    private static final UUID RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String BT_NAME = "MeyerGame";

    private final BluetoothServerSocket mmServerSocket;

    public BluetoothServerController(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
        BluetoothServerSocket tmp = null;
        try{
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BT_NAME, RFCOMM_UUID);
        } catch (IOException e){}

        mmServerSocket = tmp;
    }

    public void run(){
        mBluetoothAdapter.cancelDiscovery();
        BluetoothSocket socket = null;
        while(true){
            try{
                System.out.println("Server is listening");
                socket = mmServerSocket.accept();
            } catch (IOException e){
                break;
            }

            if(socket != null){
                System.out.println("Connection to device with mac address: " + socket.getRemoteDevice().getAddress() + " has been created!");

                //Her skal connection bruges!!!!

                break;
            }
        }
    }

    public void cancel(){
        try{
            mmServerSocket.close();
        }catch (IOException e){}
    }
}

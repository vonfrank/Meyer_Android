package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Von Frank on 10-05-2016.
 */
public class BtController extends Thread {

    private final BluetoothServerSocket mmServerSocket;

    private static final UUID RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //128bit UUID for Serial Port Protocol (SPP)
    private static final String BT_NAME = "MeyerGameServer";

    public BtController(BluetoothAdapter mBluetoothAdapter){
        BluetoothServerSocket tmp = null;

        try{
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BT_NAME, RFCOMM_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                System.out.println("Server is listening");
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}

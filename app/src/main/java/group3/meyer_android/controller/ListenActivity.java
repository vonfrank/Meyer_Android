package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import group3.meyer_android.R;
import group3.meyer_android.model.ApplicationData;

public class ListenActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothSocket> btSockets = null;
    private ApplicationData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        appData = (ApplicationData)getApplication();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            System.out.println("Device not supported");
        } else{
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 0);
            }
        }

        btSockets = new ArrayList<>();
    }

    public void startServerClick(View view) {
        new SocketConnection().execute();
    }

    public void startGameClick(View view) {
        appData.setSockets(btSockets);
        Intent newGameIntent = new Intent(this, CreateActivity.class);
        startActivity(newGameIntent);
    }

    private class SocketConnection extends AsyncTask<String, Void, BluetoothSocket> {

        private BluetoothAdapter mBluetoothAdapter;
        private UUID RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private String BT_NAME = "MeyerGame";
        private BluetoothServerSocket mmServerSocket;

        public SocketConnection() {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BT_NAME, RFCOMM_UUID);
            } catch (IOException e) {
            }

            mmServerSocket = tmp;
        }

        @Override
        protected BluetoothSocket doInBackground(String... params) {
            mBluetoothAdapter.cancelDiscovery();
            BluetoothSocket socket = null;

            while (socket == null) {
                try {
                    System.out.println("Server is listening");
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }

                if (socket != null) {
                    System.out.println("Connection to device with mac address: " + socket.getRemoteDevice().getAddress() + " has been created!");
                    return socket;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            btSockets.add(socket);
        }
    }
}
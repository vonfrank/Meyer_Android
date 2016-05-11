package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import group3.meyer_android.R;
import group3.meyer_android.view.GameFragment;

public class JoinActivity extends AppCompatActivity {

    private GameFragment gf;
    private String serverMac;
    private BluetoothSocket btSocket = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice btDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        if(savedInstanceState == null) {
            gf = new GameFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.GameContainer, gf).commit();
        }

        serverMac = getIntent().getStringExtra("mac");
        new SocketConnection(serverMac).execute();
    }

    public void nextBtnClick(View view) {
        System.out.println("Not implemented");
    }

    public void turnBtnClick(View view) {
        gf.turnBtnClick();
    }

    public void rollBtnClick(View view) {
        gf.rollBtnClick();
    }

    public void hideBtnClick(View view) {
        gf.hideBtnClick();
    }

    private class SocketConnection extends AsyncTask<String, Void, BluetoothSocket> {

        private UUID RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private BluetoothAdapter mBluetoothAdapter;
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public SocketConnection(String mac){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothSocket tmp = null;
            mmDevice = mBluetoothAdapter.getRemoteDevice(mac);

            try {
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(RFCOMM_UUID);
            } catch (IOException e){}
            mmSocket = tmp;
        }

        @Override
        protected BluetoothSocket  doInBackground(String... params) {
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                return mmSocket;
            } catch(IOException connectException){
                System.out.println("Unable to connect to server");
                try{
                    mmSocket.close();
                } catch (IOException closeException){

                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            btSocket = socket;
        }
    }
}

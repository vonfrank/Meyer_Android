package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import group3.meyer_android.R;
import group3.meyer_android.model.GameData;
import group3.meyer_android.view.GameFragment;

public class JoinActivity extends AppCompatActivity {

    private GameFragment gf;
    private String serverMac;
    private BluetoothSocket btSocket = null;
    private BufferedReader bufferedreader;
    private BufferedWriter bufferedwriter;
    private String data;

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

    private void makeInOutStreams(){
        InputStream inputstream = null;
        OutputStream outputStream = null;

        try {
            inputstream = btSocket.getInputStream();
            outputStream = btSocket.getOutputStream();
        } catch (IOException e) {
             e.printStackTrace();
            return;
        }

        bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        new RecieveText(bufferedreader).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void nextBtnClick(View view) {

        gf.nextBtnClick();

        new SendText(bufferedwriter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gf.getGameData().toJSON());
    }

    public void turnBtnClick(View view) {

        gf.turnBtnClick();;

        new SendText(bufferedwriter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gf.getGameData().toJSON());
    }

    public void rollBtnClick(View view) {
        gf.rollBtnClick();
    }

    public void hideBtnClick(View view) { gf.hideBtnClick(); }

    /**
     * Constructor takes the Buffered writer used to write.
     * The Execute takes the String to send.
     */
    private class SendText extends AsyncTask<String, Void, Void> {
        private BufferedWriter bufferedwriter = null;

        public SendText(BufferedWriter writer){
            bufferedwriter = writer;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                bufferedwriter.write(params[0]);
                System.out.println("Outgoing: " + params[0]);
                bufferedwriter.newLine();
                bufferedwriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Constructor takes the Buffered reader used to read.
     * The class sets the data variable with the result from the read.
     */
    private class RecieveText extends AsyncTask<Void, Void, String> {
        private BufferedReader bufferedreader = null;

        public RecieveText(BufferedReader reader){
            bufferedreader = reader;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;

            try {
                result = bufferedreader.readLine();
                System.out.println("Listening for incomming JSON");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            data = s;
            GameData tmp = new GameData();
            tmp.fromJSON(data);
            gf.setGameData(tmp);
            System.out.println("Incomming: " + data);
            new RecieveText(bufferedreader).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    /**
     * Constructor takes the server mac address as a parameter.
     * Returns the socket when there is s connection.
     */
    private class SocketConnection extends AsyncTask<Void, Void, BluetoothSocket> {

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
        protected BluetoothSocket  doInBackground(Void... params) {
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
            makeInOutStreams();
        }
    }
}

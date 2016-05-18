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
    private String clientMac;
    private BluetoothSocket btSocket = null;
    private BufferedReader bufferedreader;
    private BufferedWriter bufferedwritter;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
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
        bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputStream));

        try{
            ois = new ObjectInputStream(inputstream);
            oos = new ObjectOutputStream(outputStream);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void nextBtnClick(View view) {
        System.out.println("Not implemented");
    }

    public void turnBtnClick(View view) {
        //CMD 1: turn
        new SendCommand(bufferedwritter).execute(clientMac);
    }

    public void rollBtnClick(View view) {
        gf.rollBtnClick();
    }

    public void hideBtnClick(View view) {
        gf.hideBtnClick();
    }

    /**
     * Constructor takes the Buffered writer used to write.
     * The Execute takes the String to send.
     */
    private class SendCommand extends AsyncTask<String, Void, Void> {
        private BufferedWriter bufferedwriter = null;

        public SendCommand(BufferedWriter writer){
            bufferedwriter = writer;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                bufferedwriter.write(params[0]);
                bufferedwriter.newLine();
                bufferedwriter.flush();
                System.out.println("Client with mac addr called: " + params[0] + " turn successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(){
            new RecieveGameData(ois).execute();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            data = s;
        }
    }

    private class PushGameData extends AsyncTask<Object, Void, Void> {
        private ObjectOutputStream oos = null;

        public PushGameData(ObjectOutputStream ooos){ oos = ooos; }

        @Override
        protected Void doInBackground(Object... params) {
            Object gameData = null;

            try {
                oos.writeObject(gameData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class RecieveGameData extends AsyncTask<Void, Void, Object> {
        private ObjectInputStream ois = null;

        public RecieveGameData(ObjectInputStream oois) {
            ois = oois;
        }

        @Override
        protected Object doInBackground(Void... params) {
            Object tmpGameData = null;

            try {
                tmpGameData = ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return tmpGameData;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(o != null){
                gf.setGameData((GameData) o);
            }else {
                System.out.println("NOTHING WAS RECIEVED!!");
            }
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
            clientMac = mBluetoothAdapter.getAddress();
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

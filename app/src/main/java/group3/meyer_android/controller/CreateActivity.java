package group3.meyer_android.controller;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import group3.meyer_android.R;
import group3.meyer_android.model.ApplicationData;
import group3.meyer_android.model.GameData;
import group3.meyer_android.view.GameFragment;

public class CreateActivity extends AppCompatActivity {

    private GameFragment gf;
    private HashMap<BluetoothSocket, Boolean> btSockets;
    private ApplicationData appData;
    private ArrayList<BufferedReader> bufferedreaders = new ArrayList<>();
    private ArrayList<BufferedWriter> bufferedwritters = new ArrayList<>();
    private ArrayList<ObjectInputStream> objectInputStreams = new ArrayList<>();
    private ArrayList<ObjectOutputStream> objectOutputStreams = new ArrayList<>();
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        appData = (ApplicationData)getApplication();
        btSockets = appData.getSockets();

        GameData gd = new GameData();
        gd.setPlayers(btSockets);
        gf.setGameData(gd);

        setInOutStreams();

        for(int i = 0; i < gf.getGameData().getPlayers().size(); i++){
            new RecieveCommand(bufferedreaders.get(i)).execute();
        }

        if(savedInstanceState == null) {
            gf = new GameFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.GameContainer, gf).commit();
        }
    }

    private void setInOutStreams(){
        for(Entry<BluetoothSocket, Boolean> socket : gf.getGameData().getPlayers().entrySet()){
            InputStream inputstream = null;
            OutputStream outputStream = null;

            try {
                inputstream = socket.getKey().getInputStream();
                outputStream = socket.getKey().getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            bufferedreaders.add(new BufferedReader(new InputStreamReader(inputstream)));
            bufferedwritters.add(new BufferedWriter(new OutputStreamWriter(outputStream)));
            try{
                objectInputStreams.add(new ObjectInputStream(inputstream));
                objectOutputStreams.add(new ObjectOutputStream(outputStream));
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }

    public void nextBtnClick(View view) {
        gf.getGameData().setServerPlayerState(false);
    }

    public void turnBtnClick(View view) {
        gf.getGameData().setServerPlayerState(true);
    }

    public void rollBtnClick(View view) {
        gf.rollBtnClick();
    }

    public void hideBtnClick(View view) {
        gf.hideBtnClick();
    }

    public void setTurn(String data){
        if(!gf.getGameData().getPlayers().containsValue(true) && !gf.getGameData().getServerPlayerState()){
            for(Entry<BluetoothSocket, Boolean> socket : gf.getGameData().getPlayers().entrySet()){
                if(socket.getKey().getRemoteDevice().getAddress().equals(data)){
                    gf.getGameData().setPlayerState(socket.getKey(), true);
                    for(ObjectOutputStream oos : objectOutputStreams){
                        new PushGameData(oos).execute(gf.getGameData());
                    }
                }
            }
        }
    }

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
    private class RecieveCommand extends AsyncTask<Void, Void, String> {
        private BufferedReader bufferedreader = null;

        public RecieveCommand(BufferedReader reader){
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
            setTurn(data);
            new RecieveCommand(bufferedreader).execute();
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

        public RecieveGameData(ObjectInputStream oois){ ois = oois; }

        @Override
        protected Object doInBackground(Void... params) {
            Object tmpGameData = null;

            try{
                tmpGameData = ois.readObject();
            } catch(IOException | ClassNotFoundException e){
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

}

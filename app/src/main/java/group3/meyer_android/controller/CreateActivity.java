package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;

import group3.meyer_android.R;
import group3.meyer_android.model.ApplicationData;
import group3.meyer_android.view.GameFragment;

public class CreateActivity extends AppCompatActivity {

    private GameFragment gf;
    private ArrayList<BluetoothSocket> btSocketArray;
    private ApplicationData appData;
    private ArrayList<BufferedReader> bufferedreaders = new ArrayList<>();;
    private ArrayList<BufferedWriter> bufferedwritters = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        appData = (ApplicationData)getApplication();
        btSocketArray = appData.getSockets();
        setInOutStreams();

        for(int i = 0; i < btSocketArray.size(); i++){
            new SendText(bufferedwritters.get(i)).execute("test");
        }

        if(savedInstanceState == null) {
            gf = new GameFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.GameContainer, gf).commit();
        }
    }

    private void setInOutStreams(){
        for(BluetoothSocket socket : btSocketArray){
            InputStream inputstream = null;
            OutputStream outputStream = null;

            try {
                inputstream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            bufferedreaders.add(new BufferedReader(new InputStreamReader(inputstream)));
            bufferedwritters.add(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }
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

}

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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import group3.meyer_android.R;
import group3.meyer_android.model.ApplicationData;
import group3.meyer_android.view.GameFragment;

public class CreateActivity extends AppCompatActivity {

    private GameFragment gf;
    private ApplicationData appData;
    private ArrayList<BufferedReader> bufferedreaders = new ArrayList<>();
    private ArrayList<BufferedWriter> bufferedwriters = new ArrayList<>();
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        appData = (ApplicationData)getApplication();

        if(savedInstanceState == null) {
            gf = new GameFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.GameContainer, gf).commit();
        }

        setInOutStreams();

        System.out.println("Following devices has been connected:");
        for(BluetoothSocket bs : appData.getSockets()){
            System.out.println(bs.getRemoteDevice().getName());
        }


        for(int i = 0; i < appData.getSockets().size(); i++){
            new RecieveText(bufferedreaders.get(i)).execute();
        }
    }

    private void setInOutStreams(){
        for(BluetoothSocket socket : appData.getSockets()){
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                System.out.println("Could not create in- and output stream");
                return;
            }

            bufferedreaders.add(new BufferedReader(new InputStreamReader(inputStream)));
            bufferedwriters.add(new BufferedWriter(new OutputStreamWriter(outputStream)));
            System.out.println("Created in- and output stream for server");
        }
    }

    public void nextBtnClick(View view) {

    }

    public void turnBtnClick(View view) {
        for(int i = 0 ; i < appData.getSockets().size() ; i++){
            new SendText(bufferedwriters.get(i)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "herp");
        }
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
            System.out.println("Incomming: " + data);
            new RecieveText(bufferedreader).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}

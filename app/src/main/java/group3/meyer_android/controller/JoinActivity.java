package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.btDevice = btAdapter.getRemoteDevice(serverMac);

        new Thread(new BluetoothClientController(btDevice, btAdapter)).start();

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
}

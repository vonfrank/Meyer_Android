package group3.meyer_android.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Set;

import group3.meyer_android.R;
import group3.meyer_android.view.GameFragment;

public class CreateActivity extends AppCompatActivity {

    private GameFragment gf;
    private String serverMac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        if(savedInstanceState == null) {
            gf = new GameFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.GameContainer, gf).commit();
        }
        serverMac = getIntent().getStringExtra("mac");
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

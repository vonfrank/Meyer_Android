package group3.meyer_android.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group3.meyer_android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newGameClick(View view) {
        Intent it = new Intent(this, ListenActivity.class);
        startActivity(it);
    }

    public void joinGameClick(View view) {
        Intent it = new Intent(this, BluetoothListActivity.class);
        startActivity(it);
    }

    public void aboutClick(View view) {
    }
}
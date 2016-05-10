package group3.meyer_android.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group3.meyer_android.R;
import group3.meyer_android.view.BluetoothListFragment;

public class BluetoothListActivity extends AppCompatActivity {
    BluetoothListFragment bluetoothListFragment = new BluetoothListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        if(savedInstanceState == null) {
            bluetoothListFragment = new BluetoothListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.ListContainer, bluetoothListFragment).commit();
        }
    }

    public void makeVisibleClick(View view) {
        bluetoothListFragment.makeVisibleClick();
    }

    public void startGameClick(View view) {
        Intent start = new Intent(this, CreateActivity.class);
        start.putExtra("mac", bluetoothListFragment.getSelected());

        startActivity(start);
    }
}

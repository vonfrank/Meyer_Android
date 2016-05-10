package group3.meyer_android.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

import group3.meyer_android.R;

/**
 * Created by Mads on 10-05-2016.
 */
public class BluetoothListFragment extends ListFragment {

    private BluetoothAdapter BA;
    private ArrayAdapter<String> adapter;
    private ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private String selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BA = BluetoothAdapter.getDefaultAdapter();
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }

        adapter = new ArrayAdapter<>(this.getContext(), R.layout.bluetooth_list_content);
        setListAdapter(adapter);
    }

    public String getSelected(){
        return selected;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        v.setSelected(true);
        selected = devices.get(position).toString();
    }

    public  void makeVisibleClick(){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivityForResult(getVisible, 1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            new ListUpdater().execute(resultCode);
        }
    }

    private class ListUpdater extends AsyncTask<Integer, Integer, Boolean>{
        @Override
        protected Boolean  doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0] * 1000);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean done) {
            if(done) {
                adapter.clear();
                devices.clear();

                ArrayList<String> temp = new ArrayList<>();
                for(BluetoothDevice device : BA.getBondedDevices()){
                    devices.add(device);
                    temp.add(device.getName());
                }
                adapter.addAll(temp);

            }
        }
    }
}

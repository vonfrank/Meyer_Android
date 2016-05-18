package group3.meyer_android.model;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mads on 11-05-2016.
 */
public class ApplicationData extends Application {

    private ArrayList<BluetoothSocket> sockets = new ArrayList();
    private String serverMac;

    public void addSocket(BluetoothSocket socket){

        sockets.add(socket);
    }

    public void removeSocket(String address){

        for(BluetoothSocket bs : sockets){
            if(bs.getRemoteDevice().getAddress().equals(address)){
                sockets.remove(bs);
            }
        }
    }

    public ArrayList<BluetoothSocket> getSockets(){

        return sockets;
    }

    public void setSockets(ArrayList<BluetoothSocket> sockets){
        this.sockets = sockets;
    }

    public void setServerMac(String serverMac) { this.serverMac = serverMac; }

    public String getServerMac(){ return serverMac; }
}

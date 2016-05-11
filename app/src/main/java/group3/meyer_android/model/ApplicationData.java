package group3.meyer_android.model;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;

/**
 * Created by Mads on 11-05-2016.
 */
public class ApplicationData extends Application {
    private ArrayList<BluetoothSocket> sockets = new ArrayList<>();

    public void addSocket(BluetoothSocket socket){
        sockets.add(socket);
    }

    public void removeSocker(BluetoothSocket socket){
        if(sockets.contains(socket))
            sockets.remove(socket);
    }

    public ArrayList<BluetoothSocket> getSockets(){
        return sockets;
    }

    public void setSockets(ArrayList<BluetoothSocket> sockets){
        this.sockets = sockets;
    }
}

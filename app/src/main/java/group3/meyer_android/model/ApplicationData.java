package group3.meyer_android.model;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

import java.util.HashMap;

/**
 * Created by Mads on 11-05-2016.
 */
public class ApplicationData extends Application {
    private HashMap<BluetoothSocket, Boolean> sockets = new HashMap<>();

    public void addSocket(BluetoothSocket socket){
        sockets.put(socket, false);
    }

    public void removeSocket(BluetoothSocket socket){
        if(sockets.containsKey(socket))
            sockets.remove(socket);
    }

    public HashMap<BluetoothSocket, Boolean> getSockets(){
        return sockets;
    }

    public void setSockets(HashMap<BluetoothSocket, Boolean> sockets){
        this.sockets = sockets;
    }
}

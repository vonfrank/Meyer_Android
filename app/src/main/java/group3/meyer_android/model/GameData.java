package group3.meyer_android.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by Von Frank on 09-05-2016.
 */
public class GameData {

    private Die leftDie, rightDie;
    private HashMap<BluetoothSocket, Boolean> players;
    private Boolean serverPlayerState = false;
    private int checkState = 0;

    public GameData(){
        leftDie = new Die();
        rightDie = new Die();
        players = new HashMap<>();
    }

    public void setPictures(int[] pictures){
        leftDie.setPictures(pictures);
        rightDie.setPictures(pictures);
    }

    public void setPlayers(HashMap<BluetoothSocket, Boolean> players){
        this.players = players;
    }

    public HashMap<BluetoothSocket, Boolean> getPlayers(){
        return players;
    }

    public void setServerPlayerState(Boolean state){
        serverPlayerState = state;
    }

    public Boolean getServerPlayerState(){
        return serverPlayerState;
    }

    public Boolean getPlayerState(BluetoothSocket socket){
        return players.containsKey(socket);
    }

    public void setPlayerState(BluetoothSocket socket, Boolean state){
        for(Entry<BluetoothSocket, Boolean> sockets : players.entrySet()){
            if(sockets.getKey() == socket){
                sockets.setValue(state);
            }
        }
    }

    public void incrementChechState(){
        checkState++;
    }

    public int getCheckState(){
        return checkState;
    }

    public void resetCheckState(){
        checkState = 0;
    }
}

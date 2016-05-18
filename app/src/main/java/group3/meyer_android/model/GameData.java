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

    private int leftFace, rightFace;
    private String currentPlayer = null;
    private int checkState = 0;

    public GameData(){}

    public int getLeftFace() {
        return leftFace;
    }

    public void setLeftFace(int leftFace) {
        this.leftFace = leftFace;
    }

    public int getRightFace() {
        return rightFace;
    }

    public void setRightFace(int rightFace) {
        this.rightFace = rightFace;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public void setCurrentPlayer(String player){ currentPlayer = player; }

    public String getCurrentPlayer(){ return currentPlayer; }

    public void incrementChechState(){ checkState++; }

    public int getCheckState(){ return checkState; }

    public void resetCheckState(){ checkState = 0; }
}

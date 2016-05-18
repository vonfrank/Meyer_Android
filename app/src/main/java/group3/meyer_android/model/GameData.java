package group3.meyer_android.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by Von Frank on 09-05-2016.
 */
public class GameData {

    private int leftFace, rightFace;
    private String currentPlayer = "free";
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

    public String toJSON(){
        JSONObject obj = new JSONObject();

        try {
            obj.put("leftface", getLeftFace());
            obj.put("rightface", getRightFace());
            obj.put("currentplayer", getCurrentPlayer());
            obj.put("checkstate", getCheckState());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public void fromJSON(String JSONString){
        try {
            JSONObject obj = new JSONObject(JSONString);
            setLeftFace(obj.getInt("leftface"));
            setRightFace(obj.getInt("rightface"));
            if(!obj.isNull("currentplayer")){
                setCurrentPlayer(obj.getString("currentplayer"));
            }
            setCheckState(obj.getInt("checkstate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

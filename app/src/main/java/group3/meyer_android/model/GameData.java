package group3.meyer_android.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * Created by Von Frank on 09-05-2016.
 */
public class GameData {

    private boolean isChecked = true;

    public GameData(){

    }

    public boolean isChecked(){

        return isChecked;
    }

    public void setChecked(boolean isRolled){

        this.isChecked = isRolled;
    }
}

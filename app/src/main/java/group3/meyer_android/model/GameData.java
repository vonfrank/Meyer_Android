package group3.meyer_android.model;

/**
 * Created by Von Frank on 09-05-2016.
 */
public class GameData {

    private boolean isChecked = false;

    public GameData(){

    }

    public boolean isChecked(){

        return isChecked;
    }

    public void setChecked(boolean isRolled){

        this.isChecked = isRolled;
    }
}

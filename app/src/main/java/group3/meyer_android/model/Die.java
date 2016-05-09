package group3.meyer_android.model;

/**
 * Created by Von Frank on 09-05-2016.
 */
public class Die {

    private int face;
    private boolean isVisible = false;

    public Die(){

    }

    public int shuffel(){

        return 0;
    }

    public int getFace(){

        return face;
    }

    public boolean isVisible(){

        return isVisible;
    }

    public void setVisible(boolean isVisible){

        this.isVisible = isVisible;
    }
}

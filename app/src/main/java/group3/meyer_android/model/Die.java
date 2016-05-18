package group3.meyer_android.model;

import java.util.Random;

/**
 * Created by Von Frank on 09-05-2016.
 */
public class Die {

    private int face;
    private Random random;
    private int[] pictures = new int[6];
    private Boolean isVisible = false;

    public Die(){ random = new Random(); }

    public void roll(){ face = random.nextInt(5) + 1; }

    public void setPictures(int[] pictures){ this.pictures = pictures; }

    public int getFace(){ return face;}

    public int getPictureId(){ return pictures[face-1]; }

    public void setVisible(Boolean state){ isVisible = state; }

    public Boolean getVisible(){ return isVisible; }

    public void setFace(int face) {this.face = face; }
}

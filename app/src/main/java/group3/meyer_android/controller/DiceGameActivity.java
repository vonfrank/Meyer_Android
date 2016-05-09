package group3.meyer_android.controller;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import group3.meyer_android.R;
import group3.meyer_android.model.Die;
import group3.meyer_android.model.GameData;

public class DiceGameActivity extends AppCompatActivity {

    private Die dieLeft, dieRight;
    private ImageView imageLeft, imageRight;
    private Button hideButton;
    private GameData gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_game);
        initialize();
    }

    private void initialize(){
        dieLeft = new Die();
        dieRight = new Die();
        gd = new GameData();

        int[] idArray = {R.drawable.die_01, R.drawable.die_02, R.drawable.die_03, R.drawable.die_04, R.drawable.die_05, R.drawable.die_06};
        dieLeft.setPictures(idArray);
        dieRight.setPictures(idArray);
        imageLeft = (ImageView) findViewById(R.id.dieLeftImageView);
        imageRight = (ImageView) findViewById(R.id.dieRightImageView);
        hideButton = (Button) findViewById(R.id.hideBtn);
    }

    public void rollBtnClick(View view) {
        if(!dieLeft.isVisible() && !gd.isChecked()){
            dieLeft.roll();
            dieRight.roll();
            imageLeft.setImageResource(dieLeft.getPictureId());
            imageRight.setImageResource(dieRight.getPictureId());
        }
    }

    public void hideBtnClick(View view) {
        if(dieLeft.isVisible()){
            imageLeft.setImageAlpha(0);
            imageRight.setImageAlpha(0);
            dieLeft.setVisible(false);
            dieRight.setVisible(false);
            if(gd.isChecked()){
                hideButton.setText(R.string.hide_btn_call);
            }
            else{
                hideButton.setText(R.string.hide_btn_show);
            }
        } else{
            imageLeft.setImageAlpha(255);
            imageRight.setImageAlpha(255);
            dieLeft.setVisible(true);
            dieRight.setVisible(true);
            hideButton.setText(R.string.hide_btn_hide);
            gd.setChecked(true);
        }
    }

    public void turnBtnClick(View view) {
        gd.setChecked(false);

    }
}

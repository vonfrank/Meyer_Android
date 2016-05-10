package group3.meyer_android.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import group3.meyer_android.R;
import group3.meyer_android.model.Die;
import group3.meyer_android.model.GameData;

public class GameFragment extends Fragment {

    private Die dieLeft, dieRight;
    private ImageView imageLeft, imageRight;
    private Button hideButton;
    private GameData gd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize(){
        dieLeft = new Die();
        dieRight = new Die();
        gd = new GameData();

        int[] idArray = {R.drawable.die_01, R.drawable.die_02, R.drawable.die_03, R.drawable.die_04, R.drawable.die_05, R.drawable.die_06};
        dieLeft.setPictures(idArray);
        dieRight.setPictures(idArray);
        imageLeft = (ImageView) getView().findViewById(R.id.dieLeftImageView);
        imageRight = (ImageView) getView().findViewById(R.id.dieRightImageView);
        hideButton = (Button) getView().findViewById(R.id.hideBtn);
    }

    public void rollBtnClick() {
        if(!dieLeft.isVisible() && !gd.isChecked()){
            dieLeft.roll();
            dieRight.roll();
            imageLeft.setImageResource(dieLeft.getPictureId());
            imageRight.setImageResource(dieRight.getPictureId());
        }
    }

    public void hideBtnClick() {
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

    public void turnBtnClick() {
        gd.setChecked(false);
    }
}

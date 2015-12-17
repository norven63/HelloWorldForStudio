package com.myAndroid.helloworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.SeekBar;

import com.myAndroid.helloworld.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardViewActivity extends Activity {

    @Bind(R.id.cardview)
    CardView cardview;
    @Bind(R.id.cardview_radius_seekbar)
    SeekBar cardviewRadiusSeekbar;
    @Bind(R.id.cardview_elevation_seekbar)
    SeekBar cardviewElevationSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        ButterKnife.bind(this);

        cardviewRadiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardview.setRadius(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }
        });

        cardviewElevationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cardview.setElevation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }
        });
    }
}

package org.pitambar.ins.sensor;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.pitambar.ins.R;

/**
 * Created by surensth on 25/12/2022.
 */
public class InsActivity extends Activity implements SensorDataListener {
    OrientationTracker orientationTracker;
    TextView positionTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins);
        Log.v("test","ddd ");

        orientationTracker = new OrientationTracker(this);
        orientationTracker.start();

        orientationTracker.sensorDataListener = this;
        positionTextView = findViewById(R.id.posTextView);
    }

    @Override
    public void onPositionObtained(float[] position) {
        positionTextView.setText(Float.toString(orientationTracker.getPostion()[0]));
    }
}

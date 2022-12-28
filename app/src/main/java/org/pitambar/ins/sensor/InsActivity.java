package org.pitambar.ins.sensor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.pitambar.ins.R;

/**
 * Created by surensth on 25/12/2022.
 */
public class InsActivity extends Activity implements SensorDataListener {
    OrientationTracker orientationTracker;
    TextView positionTextView, orientationTextView;
    float[] deviceOrientation, devicePosition;

    RelativeLayout mChildLayout;

    DevicePositionView devicePositionView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins);
        Log.v("test","ddd ");

        orientationTracker = new OrientationTracker(this);
        orientationTracker.start();

        orientationTracker.sensorDataListener = this;
        positionTextView = findViewById(R.id.posTextView);
        orientationTextView = findViewById(R.id.orientationTextView);

        mChildLayout = findViewById(R.id.childLayout);

//        devicePositionView = new DevicePositionView(this);
//
//        mChildLayout.addView(devicePositionView);

    }

    @Override
    public void onPositionObtained(float[] position) {
        devicePosition = position;
        positionTextView.setText(Float.toString(orientationTracker.getPostion()[0])+", "+orientationTracker.getPostion()[1]+", "+orientationTracker.getPostion()[2]);

//        if(mCanvas != null){
//            devicePositionView.draw(devicePositionView.mCanvas);
////        }
    }

    @Override
    public void onOrientaionObtained(float[] orientation) {
        deviceOrientation = orientation;
        orientationTextView.setText(Float.toString(orientationTracker.getOrientation()[0])+", "+orientationTracker.getOrientation()[1]+", "+orientationTracker.getOrientation()[2]);
    }


    private class DevicePositionView extends View {

        private float[] iDeviceOrientation = {0f, 0f, 0f};
        private float[] iDevicePosition= {0f, 0f, 0f};
        private Paint paint;
        Canvas mCanvas;

        Context mContext;

        public DevicePositionView(Context context) {
            super(context);
            mContext = context;
            paint = new Paint();
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            paint.setStyle(Paint.Style.FILL);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            mCanvas = canvas;
            iDeviceOrientation = deviceOrientation;
            iDevicePosition = devicePosition;

            float x = iDevicePosition[1];
            float y = iDevicePosition[2];

            mCanvas.save();
            mCanvas.rotate(iDeviceOrientation[2], x, y);
            mCanvas.drawCircle(x, y, 5f, paint);
            mCanvas.restore();
        }
    }
}

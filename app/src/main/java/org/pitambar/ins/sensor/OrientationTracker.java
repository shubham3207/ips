package org.pitambar.ins.sensor;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by surensth on 25/12/2022.
 */
public class OrientationTracker implements SensorEventListener {

    // Declare variables to store sensor data
    float[] accelerometerData=new float[]{0f,0f,0f};
    float[] gyroscopeData=new float[]{0f,0f,0f};
    float[] magnetometerData=new float[]{0f,0f,0f};

    float[] smoothedData;
    float[] orientation;
    float[] velocity;
    float[] position;

    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor gyroscope;
    Sensor magnetometer;
    float dt = 0.525f;

public SensorDataListener sensorDataListener;

    public OrientationTracker(Context context) {

// Get the Sensor Manager and the sensor data
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    // Implement the SensorEventListener interface
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get the sensor data from the event
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData = event.values;
                Log.v("test","test accelerometerData "+accelerometerData[0]);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroscopeData = event.values;
                Log.v("test","test gyroscopeData "+gyroscopeData[0]);

                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerData = event.values;
                Log.v("test","test magnetometerData "+magnetometerData[0]);

                break;


        }


        // Use the Kalman filter or complementary filter to smooth the sensor data
        smoothedData = smoothSensorData(accelerometerData, gyroscopeData, magnetometerData);

        // Calculate the device's orientation and velocity using the smoothed data
        orientation = calculateOrientation(smoothedData);
        velocity = calculateVelocity(smoothedData);

        // Integrate the velocity data to calculate the device's position
        position = integrateVelocity(velocity);

        Log.v("test","position "+position[0]);
        sensorDataListener.onPositionObtained(position);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start() {

// Register listeners for the sensors
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }


    public float[] smoothSensorData(float[] accelerometerData, float[] gyroscopeData, float[] magnetometerData) {
        Log.v("test","test gyroscopeData obtained "+gyroscopeData[0]);


        // Create the Kalman filter
        KalmanFilter kalmanFilter = new KalmanFilter();

        // Set the initial state vector and covariance matrix
        float[] initialState = new float[6];
        float[][] initialCovariance = new float[6][6];
        kalmanFilter.setState(initialState, initialCovariance);

        // Set the measurement matrix and noise covariance matrix
        float[][] measurementMatrix = new float[6][6];
        float[][] measurementNoiseCovariance = new float[6][6];
        kalmanFilter.setMeasurement(measurementMatrix, measurementNoiseCovariance);

        // Set the process model matrix and noise covariance matrix
        float[][] processModelMatrix = new float[6][6];
        float[][] processNoiseCovariance = new float[6][6];
        kalmanFilter.setProcessModel(processModelMatrix, processNoiseCovariance);

        // Set the control input vector and control input matrix
        float[] controlInputVector = new float[6];
        float[][] controlInputMatrix = new float[6][6];
        kalmanFilter.setControlInput(controlInputVector, controlInputMatrix);

        // Set the measurement vector
        float[] measurementVector = new float[6];
        measurementVector[0] = accelerometerData[0];
        measurementVector[1] = accelerometerData[1];
        measurementVector[2] = accelerometerData[2];
        measurementVector[3] = gyroscopeData[0];
        measurementVector[4] = gyroscopeData[1];
        measurementVector[5] = gyroscopeData[2];

        // Update the Kalman filter with the new measurement
        kalmanFilter.update(measurementVector);

        // Get the smoothed sensor data from the Kalman filter
        float[] smoothedData = kalmanFilter.getState();
        return smoothedData;
    }

    private float[] calculateOrientation(float[] smoothedData) {
        // Declare variables to store the orientation
        float[] orientation = new float[3];

        // Use the accelerometer data to calculate the pitch and roll
        orientation[1] = (float) Math.atan2(-smoothedData[0], smoothedData[2]);
        orientation[2] = (float) Math.atan2(-smoothedData[1], smoothedData[2]);

        // Use the gyroscope data to calculate the yaw
        orientation[0] = smoothedData[3] * dt + orientation[0];

        // Return the orientation
        return orientation;
    }

    public float[] calculateVelocity(float[] smoothedData) {
        // Declare variables to store the velocity
        float[] velocity = new float[3];

        // Use the accelerometer data to calculate the velocity
        velocity[0] = smoothedData[0] * dt + velocity[0];
        velocity[1] = smoothedData[1] * dt + velocity[1];
        velocity[2] = smoothedData[2] * dt + velocity[2];

        // Return the velocity
        return velocity;
    }

    public float[] integrateVelocity(float[] velocity) {
        // Declare variables to store the position
        float[] position = new float[3];

        // Integrate the velocity data to calculate the position
        position[0] = velocity[0] * dt + position[0];
        position[1] = velocity[1] * dt + position[1];
        position[2] = velocity[2] * dt + position[2];

        // Return the position
        return position;
    }

    public float[] getPostion(){
        return position;
    }
}

interface SensorDataListener{
    public void onPositionObtained(float[] position);
}
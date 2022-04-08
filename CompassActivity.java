package com.example.hellosensor2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.hardware.SensorEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.os.Bundle;
public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    // device sensor manager
    private SensorManager sensorManager;
    // define the compass picture that will be use
    private ImageView compassimage;

    private int mAzimuth;
    private TextView degreeTV;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    private float [] rMat = new float [9];
    private float [] orientation = new float [9];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        compassimage = (ImageView) findViewById(R.id.compass_image);
        degreeTV = (TextView) findViewById(R.id.DegreeTV);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        start();
    }

    private void start() {
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this,mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop (){
        sensorManager.unregisterListener(this,mAccelerometer);
        sensorManager.unregisterListener(this, mMagnetometer);

    }
    @Override
    protected void onResume() {
        super.onResume();
        // code for system's orientation sensor registered listeners
       start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        stop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values,0,mLastAccelerometer,0,event.values.length);
            mLastAccelerometerSet = true;
        } else
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values,0,mLastMagnetometer,0,event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            sensorManager.getRotationMatrix(rMat,null,mLastAccelerometer,mLastMagnetometer);
            sensorManager.getOrientation(rMat,orientation);
            mAzimuth = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat,orientation) [0]) + 360) % 360);
        }
        mAzimuth = Math.round(mAzimuth);
        compassimage.setRotation(-mAzimuth);
        degreeTV.setText("Heading: " + mAzimuth + " degrees");

        if (mAzimuth >= 345 || mAzimuth <= 15) {
            degreeTV.setText("Norr");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
package com.example.hellosensor2;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MotionActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager SensorManager;
    private Sensor acc;
    private TextView x_koord;
    private TextView y_koord;
    private TextView z_koord;
    private TextView riktning;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

        x_koord = findViewById(R.id.x_koord);
        y_koord = findViewById(R.id.y_koord);
        z_koord = findViewById(R.id.z_koord);
        riktning = findViewById(R.id.riktning);
        SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc = SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        SensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // code for system's orientation sensor registered listeners
        SensorManager.registerListener(this, acc,
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        x_koord.setText("X: " + event.values[0]);
        y_koord.setText("Y: " + event.values[1]);
        z_koord.setText("Z: " + event.values[2]);
        if (event.values[0] < -1.0f) {
            getWindow().getDecorView().setBackgroundColor(Color.RED);
            riktning.setText("Höger");
        } else if (event.values[0] > 1.0f) {
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            riktning.setText("Vänster");
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            riktning.setText("Mitten");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
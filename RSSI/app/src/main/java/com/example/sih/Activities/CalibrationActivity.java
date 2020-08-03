package com.example.sih.Activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.sih.R;
import com.google.android.material.snackbar.Snackbar;
import java.util.HashMap;
import java.util.Map;

public class CalibrationActivity extends AppCompatActivity implements SensorEventListener {

    HashMap<String, Pair<Integer, Integer>> map;
    Button confirm, done;
    TextView altitude, display, record;
    EditText floorTv;
    float height;
    private SensorManager sensorManager;
    private Sensor pressure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        //Initialize
        map = new HashMap<>();
        confirm = findViewById(R.id.confirm);
        done = findViewById(R.id.done);
        altitude = findViewById(R.id.altitude);
        record = findViewById(R.id.record);
        floorTv = findViewById(R.id.floorTv);
        display = findViewById(R.id.display);
        height = 0.0f;
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        display.setVisibility(View.INVISIBLE);
        record.setVisibility(View.INVISIBLE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( floorTv.getText().toString().isEmpty() ){
                    ConstraintLayout layout = findViewById(R.id.calibrate_layout);
                    Snackbar snackbar = Snackbar.make(layout, "Please write floor number", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    map.put(floorTv.getText().toString(), new Pair( (int)height, (int)height) );
                    floorTv.setText("");
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display.setVisibility(View.VISIBLE);
                record.setVisibility(View.VISIBLE);
                confirm.setEnabled(false);
                done.setEnabled(false);

                String res = "Recorded Values:\n";

                for (Map.Entry mapElement : map.entrySet()) {
                    String key = (String)mapElement.getKey();
                    Pair<Integer, Integer> temp = (Pair<Integer, Integer>) mapElement.getValue();
                    int x = temp.first - 1;
                    res = res + "Floor " + key + ": " + x + ".5 to " + temp.second + ".5 \n";
                }
                Log.e("shivam", res);
                record.setText(res);
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        synchronized (this){
            if( event.sensor.getType() == Sensor.TYPE_PRESSURE ){

                altitude.setTextColor(getResources().getColor(R.color.black));
                height = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, event.values[0]);
                altitude.setText("Altitude : " + height + " m");

                for (Map.Entry mapElement : map.entrySet()) {
                    String key = (String)mapElement.getKey();
                    Pair<Integer, Integer> temp = (Pair<Integer, Integer>) mapElement.getValue();
                    int x = (int)temp.first - 1;
                    int y = (int)temp.second + 1;
                    if( (int)height>=x && (int)height<=y ){
                        display.setText("You are on floor " + key);
                        Log.e("shivam", "You are on floor " + key);
                    }
                }

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}

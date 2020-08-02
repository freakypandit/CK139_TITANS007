package com.thirstymission.stepcounter;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private CSVWriter csvWriter;
    private File path;
    private float[] prev = {0f,0f,0f};
    private File file;
    private Menu menu;
    private TextView stepView,navigate;
    private int stepCount = 0;
    private static final int ABOVE = 1;
    private static final int BELOW = 0;
    private static int CURRENT_STATE = 0;
    private static int PREVIOUS_STATE = BELOW;
    private int sampleCount = 0;
    private long startTime;
    boolean SAMPLING_ACTIVE = true;
    private long streakStartTime;
    private long streakPrevTime;
    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;


    //hardcode
    int totaldistance=65;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepView = (TextView) findViewById(R.id.count);
        navigate = (TextView) findViewById(R.id.direction);
        path =  this.getExternalFilesDir(null);
        file = new File(path, "raghu1.csv");
        try {
            csvWriter = new CSVWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        streakPrevTime = System.currentTimeMillis() - 500;


        camera=Camera.open();
        showCamera=new ShowCamera(this,camera);
        frameLayout.addView(showCamera);


    }




    @Override
    protected void onResume() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.instrumentation) {
            SAMPLING_ACTIVE = true;
            sampleCount = 0;
            startTime = System.currentTimeMillis();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleEvent(SensorEvent event) {
        prev = lowPassFilter(event.values,prev);
        Accelerometer data = new Accelerometer(prev);
        StringBuilder text = new StringBuilder();
        text.append("X: " + data.X);
        text.append("Y: " + data.Y);
        text.append("Z: " + data.Z);
        text.append("R: " + data.R);
        if(data.R > 10.5f){
            CURRENT_STATE = ABOVE;
            if(PREVIOUS_STATE != CURRENT_STATE) {
                streakStartTime = System.currentTimeMillis();
                if ((streakStartTime - streakPrevTime) <= 250f) {
                    streakPrevTime = System.currentTimeMillis();
                    return;
                }
                streakPrevTime = streakStartTime;
                Log.d("STATES:", "" + streakPrevTime + " " + streakStartTime);
                stepCount++;
            }
            PREVIOUS_STATE = CURRENT_STATE;
        }
        else if(data.R < 10.5f) {
            CURRENT_STATE = BELOW;
            PREVIOUS_STATE = CURRENT_STATE;
        }
        stepView.setText(""+(stepCount));
        room414();



    }

    private float[] lowPassFilter(float[] input, float[] prev) {
        float ALPHA = 0.1f;
        if(input == null || prev == null) {
            return null;
        }
        for (int i=0; i< input.length; i++) {
            prev[i] = prev[i] + ALPHA * (input[i] - prev[i]);
        }
        return prev;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleEvent(event);
            if(SAMPLING_ACTIVE) {
                long now = System.currentTimeMillis();
                if (now >= startTime + 5000) {
                    double samplingRate = sampleCount / ((now - startTime) / 1000.0);
                    SAMPLING_ACTIVE = false;
                    Toast.makeText(getApplicationContext(), "Sampling rate of your device is " + samplingRate + "Hz", Toast.LENGTH_LONG).show();
                    MenuItem rate = menu.findItem(R.id.instrumentation);
                    rate.setTitle("Sampling Rate : " + samplingRate + "hz");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public void room414()
    {
        if(stepCount>=0 && stepCount<=2)
        {
            navigate.setText("^");
        }
        else if(stepCount==3)
        {
            navigate.setText(">>>");
        }


        else if(stepCount>=4 && stepCount<=15)
        { navigate.setText("^");}

        else if(stepCount==16)
        {
            navigate.setText("<<<");
        }


        else if(stepCount>=17 && stepCount<=60)
        { navigate.setText("^");}

        else if(stepCount==61)
        {
            navigate.setText("<<<");
        }


        else if(stepCount>=61 && stepCount<=63)
        { navigate.setText("^");}

        else if(stepCount==64)
        {
            navigate.setText("  Arrived  414");
        }

    }
}

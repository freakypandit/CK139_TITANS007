package com.example.sih.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sih.R;
import java.util.ArrayList;
import static java.lang.StrictMath.abs;

public class RssiActivity extends AppCompatActivity implements SensorEventListener {

    //Variables
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    Sensor pressure;
    float[] mGravity;
    float[] mGeomagnetic;

    private float rssiValue;
    private float rssiDistance;
    private float dirAngle;

    private float prevAngle;
    private float currentAngle;

    private Pair< Float, Float > ans;

    private float associatedDistance;

    ArrayList<Pair<Float, Float>> pairOfRssiAndDirection;

    Button scan, complete_scan;
    TextView rssi, distance, direction, altitude;
    ImageView dial, hands;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssi);

        //Initializing
        scan = findViewById(R.id.scan);
        complete_scan = findViewById(R.id.complete_scan);
        rssi = findViewById(R.id.rssiTv);
        distance = findViewById(R.id.distanceTv);
        direction = findViewById(R.id.directionTv);
        altitude = findViewById(R.id.altitudeTv);
        dial = findViewById(R.id.dial);
        hands = findViewById(R.id.hands);

        pairOfRssiAndDirection = new ArrayList<>();
        mGravity = new float[3];
        mGeomagnetic = new float[3];

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        pressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        associatedDistance = 0;

        //Method Calls
        scanning();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float alpha = 0.97f;

        synchronized (this){
            if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
                mGravity[0] = alpha*mGravity[0] + (1-alpha)*event.values[0];
                mGravity[1] = alpha*mGravity[1] + (1-alpha)*event.values[1];
                mGravity[2] = alpha*mGravity[2] + (1-alpha)*event.values[2];
            }

            if( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ) {
                mGeomagnetic[0] = alpha*mGeomagnetic[0] + (1-alpha)*event.values[0];
                mGeomagnetic[1] = alpha*mGeomagnetic[1] + (1-alpha)*event.values[1];
                mGeomagnetic[2] = alpha*mGeomagnetic[2] + (1-alpha)*event.values[2];
            }

            if( event.sensor.getType() == Sensor.TYPE_PRESSURE ){
                altitude.setText("" + SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, event.values[0]));
                Log.e("shivam", "" + SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, event.values[0]));
            }

            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);

            if(success){
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                currentAngle = (float)Math.toDegrees(orientation[0]);
                currentAngle = (currentAngle + 360) % 360;

                direction.setText("Direction: "+ String.valueOf(currentAngle) );

                Animation anim = new RotateAnimation(-prevAngle, -currentAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                prevAngle = currentAngle;

                anim.setDuration(200);
                anim.setRepeatCount(0);
                anim.setFillAfter(true);

                dial.startAnimation(anim);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void scanning(){

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWifi();
                setValues();
                pairOfRssiAndDirection.add( new Pair<Float, Float>(rssiValue,dirAngle) );
                scanning();
            }
        });

        complete_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMaximum(pairOfRssiAndDirection);
            }
        });

    }

    public void checkWifi(){
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(wifiMgr.isWifiEnabled()){
            WifiInfo Info = wifiMgr.getConnectionInfo();
            if( Info.getNetworkId() == -1 ){
//                Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
            }
            else{
                calculateRssiAndDistance();
            }
        }
        else{
//            Toast.makeText(this,"Turn On Your WiFi!",Toast.LENGTH_LONG).show();
        }
    }

    public void calculateRssiAndDistance(){
        WifiManager wifiCont = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        rssiValue = wifiCont.getConnectionInfo().getRssi();
        rssiDistance= (int) GetDistanceFromRssiAndTxPowerOn1m(rssiValue,-45);
    }

    public float getRssiValue(){
        WifiManager wifiCont = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiCont.getConnectionInfo().getRssi();
    }

    public float GetDistanceFromRssiAndTxPowerOn1m(float rssiValue, int txPower){
        return (float)Math.pow(10, (txPower - rssiValue) / (10 * 2));
    }

    public void getMaximum(ArrayList < Pair <Float,Float> > pairOfRssiAndDirection)
    {
        float max = Float.MAX_VALUE;
        ans = new Pair<Float, Float>(0.0f, 0.0f);

        for(int i=0; i<pairOfRssiAndDirection.size(); i++){
            float temp = abs(pairOfRssiAndDirection.get(i).first);                           //Rssi
            if(temp<max){
                max = temp;
                ans = pairOfRssiAndDirection.get(i);
            }
        }

        pairOfRssiAndDirection.removeAll(pairOfRssiAndDirection);

        associatedDistance = GetDistanceFromRssiAndTxPowerOn1m(ans.first,-45);

        rssi.setText(String.valueOf(ans.first));
        distance.setText(String.valueOf(associatedDistance));
        direction.setText(String.valueOf(ans.second));

        adjustArrow( ans.second );

        scanning();
    }

    private void adjustArrow( float b ) {
        Animation an = new RotateAnimation(0.0f, abs((float)(b-dirAngle)),Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);

        an.setRepeatCount(0);
        an.setDuration(1000);
        an.setFillAfter(true);

        hands.startAnimation(an);
    }

    @SuppressLint("SetTextI18n")
    public void setValues(){
        rssi.setText("Rssi Value: "+ getRssiValue());
        float dis = GetDistanceFromRssiAndTxPowerOn1m(getRssiValue(), -45);
        distance.setText("Approximate Distance: "+ String.valueOf(dis));
    }

}

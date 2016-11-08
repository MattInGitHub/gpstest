package com.matt.gpstest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class SatelliteActivity extends Activity {
    TestView testView;
    private float[] mValues;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        testView = (TestView) findViewById(R.id.test_view);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mListener, mSensor,
                SensorManager.SENSOR_DELAY_GAME);
//		Typeface fontFace = Typeface.createFromAsset(((Activity)context).getAssets(),
//				"字酷堂海藏楷体.ttf");
//		mTextPaint.setTypeface(fontFace);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(mListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            mValues = event.values;
            if(mValues!=null){
                testView.refreshView(mValues[0]);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}

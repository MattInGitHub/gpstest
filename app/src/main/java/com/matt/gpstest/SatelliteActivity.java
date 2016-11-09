package com.matt.gpstest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class SatelliteActivity extends Activity {
    Context mContext;
    TestView testView;
    private float[] mValues;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    LocationManager mLocationManager;
    LocationListenerImpl mLocationListenerImpl;
    GpsStatusListener mGpsStatusListener;

    private class GpsStatusListener implements GpsStatus.Listener {
        @Override
        public void onGpsStatusChanged(int event) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
            ArrayList<GpsSatellite> mSatelliteList = new ArrayList<>();
            if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS)
            {
                Log.e("AAAA","IN");
                //获取最大的卫星数（这个只是一个预设值）
                int maxSatellites = gpsStatus.getMaxSatellites();
                Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
                mSatelliteList.clear();
                //记录实际的卫星数目
                int count = 0;
                while (it.hasNext() && count <= maxSatellites)
                {
                    //保存卫星的数据到一个队列，用于刷新界面
                    GpsSatellite s = it.next();
                    mSatelliteList.add(s);
                    count++;
                }
                testView.drawSatellite(mSatelliteList);
            }
        }
    }

    private class LocationListenerImpl implements LocationListener {

        //当设备位置发生变化时调用该方法
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // 获取经度
                double longitude = location.getLongitude();
                // 获取纬度
                double altitude = location.getAltitude();
                String message = "经度为:" + longitude + "\n" + "纬度为:" + altitude;
                Log.e("AAAA",message);
            }
        }

        //当provider的状态发生变化时调用该方法.比如GPS从可用变为不可用.
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //当provider被打开的瞬间调用该方法.比如用户打开GPS
        @Override
        public void onProviderEnabled(String provider) {

        }

        //当provider被关闭的瞬间调用该方法.比如关闭打开GPS
        @Override
        public void onProviderDisabled(String provider) {

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        testView = (TestView) findViewById(R.id.test_view);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        this.mContext = this;
        intLoc();
    }

    private void intLoc() {
        mLocationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //注册位置监听
        mLocationListenerImpl = new LocationListenerImpl();
        mGpsStatusListener = new GpsStatusListener();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, mLocationListenerImpl);
        mLocationManager.addGpsStatusListener(mGpsStatusListener);
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
                testView.rotateView(mValues[0]);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}

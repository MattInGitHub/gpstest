package com.matt.gpstest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class TestActivity extends AppCompatActivity {
    TextView locationView;
    TextView gpsView;
    Context mContext;

    SatelliteView satelliteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initLoc();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mContext =this;
        locationView = (TextView) findViewById(R.id.locationinfo);
        gpsView = (TextView) findViewById(R.id.gpsinfo);
        satelliteView = (SatelliteView) findViewById(R.id.satelliteView);

    }


    LocationManager mLocationManager;
    LocationListenerImpl mLocationListenerImpl;
    GpsStatusListener mGpsStatusListener;

    private void initLoc() {
        mLocationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
/*
        //获取可用的位置信息Provider.即passive,network,gps中的一个或几个
        List<String> providerList = mLocationManager.getProviders(true);
        for (Iterator<String> iterator = providerList.iterator(); iterator.hasNext(); ) {
            String provider = (String) iterator.next();
            System.out.println("provider=" + provider);
        }
        */
        //在此采用GPS的方式获取位置信息
        String GPSProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        Location location = mLocationManager.getLastKnownLocation(GPSProvider);
//        if (location != null) {
//            double longitude = location.getLongitude();
//            double altitude = location.getAltitude();
//            System.out.println("longitude=" + longitude + ",altitude=" + altitude);
//        }
        //注册位置监听
        mLocationListenerImpl = new LocationListenerImpl();
        mGpsStatusListener = new GpsStatusListener();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, mLocationListenerImpl);
        mLocationManager.addGpsStatusListener(mGpsStatusListener);
    }

    private class GpsStatusListener implements GpsStatus.Listener {
        @Override
        public void onGpsStatusChanged(int event) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
            showGpsStatus(event,gpsStatus);
        }
    }

    private void showGpsStatus(int event, GpsStatus gpsStatus) {
        ArrayList<GpsSatellite> numSatelliteList = new ArrayList<>();
        if (gpsStatus == null)
        {
        }else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS)
        {
            String tempString = "";
            //获取最大的卫星数（这个只是一个预设值）
            int maxSatellites = gpsStatus.getMaxSatellites();
            Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
            numSatelliteList.clear();
            //记录实际的卫星数目
            int count = 0;
            while (it.hasNext() && count <= maxSatellites)
            {
                //保存卫星的数据到一个队列，用于刷新界面
                GpsSatellite s = it.next();
                tempString +="\n编号:"+s.getPrn()+" 信噪比:"+s.getSnr()+" 方位角:"+s.getAzimuth()+" 高度角:"+s.getElevation()+s.hasAlmanac();
                numSatelliteList.add(s);
                count++;
            }
            satelliteView.drawGps(numSatelliteList);
            gpsView.setText(tempString);
        }
        else if(event==GpsStatus.GPS_EVENT_STARTED)
        {
            //定位启动
        }
        else if(event==GpsStatus.GPS_EVENT_STOPPED)
        {
            //定位结束
        }
    }


    private class LocationListenerImpl implements LocationListener {

        //当设备位置发生变化时调用该方法
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                showLocation(location);
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


    private void showLocation(Location location) {
        // 获取经度
        double longitude = location.getLongitude();
        // 获取纬度
        double altitude = location.getAltitude();
        String message = "经度为:" + longitude + "\n" + "纬度为:" + altitude;
        locationView.setText(message);
        System.out.println(message);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.removeUpdates(mLocationListenerImpl);
        }
    }
}

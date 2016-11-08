package com.matt.gpstest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

//gps星历图
public class TestView extends View {

	//星图的X，y坐标
	int originX = 100;
	int originY =100;
	float density;
	int radius = 100;
	int[] lines;
	//卫星画笔
	Paint mPointPaint = null;
	//文字画笔
	Paint mTextPaint = null;

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private float[] mValues;

	public TestView(Context context) {
		this(context,null);
	}
	public TestView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private final SensorEventListener mListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			mValues = event.values;
			invalidate();
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	private void initView(Context context) {
		Typeface fontFace = Typeface.createFromAsset(((Activity)context).getAssets(),
				"字酷堂海藏楷体.ttf");


		mSensorManager = (SensorManager)((Activity)context).getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener(mListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(200);
//		mTextPaint.setStyle(Paint.Style.STROKE);
		mTextPaint.setStrokeWidth(100);
		mTextPaint.setTypeface(fontFace);
		mTextPaint.setColor(Color.BLACK);

		DisplayMetrics metric = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）

		mPointPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointPaint.setStyle(Paint.Style.STROKE);
		mPointPaint.setColor(Color.RED);
		mPointPaint.setStrokeWidth(2);


	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate(originX*density,originY*density);
		if (mValues != null) {
			canvas.rotate(-mValues[0]);
		}

//		RectF rectF=new RectF(0,0,200*density,200*density);
//		canvas.drawArc(rectF, 0, 30, false, mPointPaint);
		canvas.drawCircle(0,0 , radius*density,mPointPaint);

		PathEffect effects = new DashPathEffect(new float[] { 5, 5 }, 1);
		mPointPaint.setPathEffect(effects);

//		PathEffect effects = new DashPathEffect(new float[] { 5, 5 }, 1);
//		mPointPaint.setPathEffect(effects);
//		radius = (int) (radius*density);
		for(int i = 1;i<3;i++){
			canvas.drawCircle(0,0, (float) ((i/3.00)*radius*density),mPointPaint);
		}

		Path path = new Path();
		path.reset();

		for(int i = 1;i<13;i++){
			double x = radius*density*Math.cos(Math.toRadians(30*i));
			double y = radius*density*Math.sin(Math.toRadians(30*i));
			path.moveTo((float)x,(float)y);
			path.lineTo(0,0);
		}
		path.close();

		canvas.drawText("卍",-100,70/*-80*density*/, mTextPaint);
		canvas.drawPath(path,mPointPaint);



	}


}

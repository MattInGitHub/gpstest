package com.matt.gpstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

//gps星历图
public class TestView extends View {

	//星图的X，y坐标
	int originX = 100;
	int originY =100;
	//星图的半径
	int viewR = 0;
	//卫星点
	ArrayList<GpsSatellite> satellitesList;

	//卫星点半径
	float PI = 3.14159f;
	//卫星画笔
	Paint mPointPaint = null;
	//文字画笔
	Paint mTextPaint = null;

	public TestView(Context context) {
		this(context,null);
	}
	public TestView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(30);
		mTextPaint.setStrokeWidth(1);
		mTextPaint.setColor(0xff000000);
		PathEffect effects = new DashPathEffect(new float[] { 2, 2 }, 1);

		mPointPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointPaint.setStyle(Paint.Style.STROKE);
		mPointPaint.setColor(Color.LTGRAY);
		mPointPaint.setStrokeWidth(2);
		mPointPaint.setPathEffect(effects);

	}


	@Override
	protected void onDraw(Canvas canvas) {
		RectF rectF=new RectF(0,0,200,200);
		super.onDraw(canvas);
		canvas.drawCircle(originX,originY , 8,mPointPaint);
		canvas.drawArc(rectF, 0, 30, false, mPointPaint);
	}

	
}

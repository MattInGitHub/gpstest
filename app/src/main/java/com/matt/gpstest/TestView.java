package com.matt.gpstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

//gps星历图
public class TestView extends View {

	//星图的X，y坐标
	int originX;
	int originY;
	int radius;
	//卫星画笔
	Paint mNetPaint = null;
	//文字画笔
	Paint mTextPaint = null;
	Path netPath;
	float mValue;

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

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//最终尺寸
		int width = 0;
		int height = 0;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = widthSize;
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = heightSize;
		}
		int Diameter = height<=width?height:width;//圆形图表的半径
		radius = Diameter/2;
		originX = radius;
		originY =radius;
		setMeasuredDimension(Diameter, Diameter);
	}




	private void initView(Context context) {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(100);
		mTextPaint.setStyle(Paint.Style.STROKE);
		mTextPaint.setStrokeWidth(2);
		mTextPaint.setColor(Color.BLACK);

		mNetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mNetPaint.setStyle(Paint.Style.STROKE);
		mNetPaint.setColor(Color.RED);
		mNetPaint.setStrokeWidth(2);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5 }, 1);
		mNetPaint.setPathEffect(effects);
	}

	public void refreshView(float SensorValue){
		this.mValue = SensorValue;
		invalidate();
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.translate(originX,originY);
		canvas.rotate(-mValue);

		canvas.drawCircle(0,0 , radius, mNetPaint);

		for(int i = 1;i<3;i++){
			canvas.drawCircle(0,0, (float) ((i/3.00)*radius), mNetPaint);
		}

		canvas.drawText("Cool",-100,45, mTextPaint);

		netPath = new Path();
		netPath.reset();
		for(int i = 1;i<13;i++){
			double x = radius*Math.cos(Math.toRadians(30*i));
			double y = radius*Math.sin(Math.toRadians(30*i));
			netPath.moveTo((float)x,(float)y);
			netPath.lineTo(0,0);
		}
		netPath.close();
		canvas.drawPath(netPath, mNetPaint);
	}
}

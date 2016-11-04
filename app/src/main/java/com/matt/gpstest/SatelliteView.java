package com.matt.gpstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

//gps星历图
public class SatelliteView extends View {

	//星图的X，y坐标
	int originX = 0;
	int originY =0;
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
	
	public SatelliteView(Context context) {
		this(context,null);
	}
	public SatelliteView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public SatelliteView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(30);
		mTextPaint.setStrokeWidth(1);
		mTextPaint.setColor(0xff000000);

		mPointPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointPaint.setARGB(155, 167, 190, 206);
		mPointPaint.setStrokeWidth(2);
	}

	public void drawGps(ArrayList<GpsSatellite> satellitesList) {
			this.satellitesList =satellitesList;
			invalidate();
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
		}
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
		}

		originX = width/2;
		originY =height/2;
		viewR = originX<=originY?originX:originY;//圆形图表的半径
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(this.satellitesList == null){
			return;
		}
		int num = this.satellitesList.size();
		if(num>=9){
			num = 9;
		}
		Point satellitePos[] =new Point[10];
		System.out.println("satellitePos num=" +num);
		//根据方向角和高度角计算出，卫星显示的位置
		//point就是你需要绘画卫星图的起始坐标
		canvas.drawCircle(originX,originY , 8,mPointPaint);
		for(int i=0;i<num;i++){
			satellitePos[i]=new Point(0,0);
			double dRadius = Math.cos(this.satellitesList.get(i).getElevation() * PI / 180) * viewR ;
			satellitePos[i].x = originX +(int)(Math.sin(this.satellitesList.get(i).getAzimuth() * PI / 180)*dRadius);
			satellitePos[i].y = originY -(int)(Math.cos(this.satellitesList.get(i).getAzimuth() * PI / 180)*dRadius);
			canvas.drawText(""+this.satellitesList.get(i).getPrn(),satellitePos[i].x,satellitePos[i].y , mTextPaint);
			mPointPaint.setARGB(155, 167, 190, 206);
			canvas.drawCircle(satellitePos[i].x,satellitePos[i].y , 8,mPointPaint);
			mPointPaint.setARGB(255, 255, 0, 0);
			canvas.drawCircle(satellitePos[i].x,satellitePos[i].y , 5,mPointPaint);

		}
	}

	
}

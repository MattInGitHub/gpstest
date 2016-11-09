package com.matt.gpstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

//gps星历图
public class TestView extends View {

    //星图的X，y坐标
    int originX;
    int originY;
    int radius;
    //卫星点半径
    float PI = 3.14159f;
    //卫星点
    ArrayList<GpsSatellite> satellitesList;

    //圆环画笔
    Paint mCirclePaint = null;
    //网图画笔
    Paint mNetPaint = null;
    //文字画笔
    Paint mTextPaint = null;
    //卫星画笔
    Paint mPointPaint = null;
    //网图路径
    Path netPath;
    //旋转角度
    float mValue;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
        int square = height <= width ? height : width;//圆形图表的半径
        int Diameter = square - 10;//圆形图表的半径
        radius = Diameter / 2;
        originX = square / 2;
        originY = square / 2;
        setMeasuredDimension(square, square);
    }


    private void initView(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(30);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(Color.WHITE);

        mNetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNetPaint.setStrokeWidth((float) 0.8);
        mNetPaint.setStyle(Paint.Style.STROKE);
        mNetPaint.setColor(0xffffffff);
        PathEffect effects = new DashPathEffect(new float[]{6, 6}, 1);
        mNetPaint.setPathEffect(effects);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(0xff000040);

        mPointPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setColor(Color.RED);
        mPointPaint.setStrokeWidth(2);
    }

    public void rotateView(float SensorValue) {
        this.mValue = SensorValue;
        invalidate();
    }

    public void drawSatellite(ArrayList<GpsSatellite> satellitesList) {
        this.satellitesList =satellitesList;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(originX, originY);
        canvas.rotate(-mValue);

        canvas.drawCircle(0, 0, radius, mCirclePaint);

        for (int i = 1; i < 4; i++) {
            canvas.drawCircle(0, 0, (float) ((i / 3.00) * radius), mNetPaint);
        }

        netPath = new Path();
        netPath.reset();
        for (int i = 1; i < 13; i++) {
            double x = radius * Math.cos(Math.toRadians(30 * i));
            double y = radius * Math.sin(Math.toRadians(30 * i));
            netPath.moveTo((float) x, (float) y);
            netPath.lineTo(0, 0);
        }
        netPath.close();
        canvas.drawPath(netPath, mNetPaint);

        canvas.drawText("N", -10, -150, mTextPaint);
        canvas.drawText("180°", -10, 150, mTextPaint);
        canvas.drawText("270°", -200, 10, mTextPaint);
        canvas.drawText("90°", 150, 10, mTextPaint);



        if(this.satellitesList == null){
            return;
        }
        int num = this.satellitesList.size();
        if(num>=9){
            num = 9;
        }
        Point satellitePos[] =new Point[10];
        //根据方向角和高度角计算出，卫星显示的位置
        for(int i=0;i<num;i++){
            satellitePos[i]=new Point(0,0);
            double dRadius = Math.cos(this.satellitesList.get(i).getElevation() * PI / 180) * radius ;
            satellitePos[i].x = (int)(Math.sin(this.satellitesList.get(i).getAzimuth() * PI / 180)*dRadius);
            satellitePos[i].y = -(int)(Math.cos(this.satellitesList.get(i).getAzimuth() * PI / 180)*dRadius);
            canvas.drawText(""+this.satellitesList.get(i).getPrn(),satellitePos[i].x,satellitePos[i].y , mTextPaint);
            canvas.drawCircle(satellitePos[i].x,satellitePos[i].y , 8,mPointPaint);
        }
    }
}

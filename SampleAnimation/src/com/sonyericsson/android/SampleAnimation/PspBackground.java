package com.sonyericsson.android.SampleAnimation;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PspBackground extends SurfaceView implements SurfaceHolder.Callback, Runnable
{	
	private Thread mThread;
	private SurfaceHolder mSurfaceHolder;
	private int mWidth;
	private int mHeight;
	
	private  static final int MAX_CONTROL_DEV = 130;
	private static final int MAX_POINT_DEV = 50;
	private static final int MAX_CONTROL_X_DEV = 40;
	private static final int MAX_MID_X_DEV = 20;
	private static final int N_CURVES = 2;
	private static final int OUT_OF_BOUND = 20;

	private boolean mShowPoints = true;
	
	Curve [] mCurves = null;
	Paint [] mCurvePaint = null;
	Paint [] mCurveStroke = null;
	
	private Paint mBgPaint, mPointPaint;
	private int mTicker;
	private Bitmap mbgBitmap;
	
	// Curve storage
	private class Curve 
	{	
		public int startVector = 1,startCounter = 0;
		public int startSpeed = 2;
		
		public int endVector = 1,endCounter = 0;
		public int endSpeed = 2;
		
		public int midVector = 1,midCounter = 0;
		public int midSpeed = 2;
		
		public int c1Vector = 1, c1Counter = 0;
		public int c4Vector = 1, c4Counter = 0;
		
		public Curve()
		{
			Random random = new Random();
			
			c1Counter = random.nextInt(2*MAX_CONTROL_DEV)-MAX_CONTROL_DEV;
			c4Counter = random.nextInt(2*MAX_CONTROL_DEV)-MAX_CONTROL_DEV;
			
			startCounter = -random.nextInt(MAX_POINT_DEV);
			startVector = 1;
			
			midCounter = random.nextInt(2*MAX_POINT_DEV)-MAX_POINT_DEV;
			
			endCounter = random.nextInt(MAX_POINT_DEV);
			startVector = -1;
			
		}
		
		// Makes sure none of our points are out of wack, switches direction if so
		public void boundsCheck()
		{
			if( startVector > 0 && startCounter >  MAX_POINT_DEV || 
				startVector < 0 && startCounter <  -MAX_POINT_DEV ) 
			{
				startVector *= -1;
			}
			
			if( c1Vector > 0 && c1Counter > MAX_CONTROL_DEV || 
				c1Vector < 0 && c1Counter < -MAX_CONTROL_DEV ) 
			{
				c1Vector *= -1;
			}
			
			if( midVector > 0 && midCounter >  MAX_POINT_DEV || 
				midVector < 0 && midCounter <  -MAX_POINT_DEV ) 
			{
				midVector *= -1;
			}
			
			if( c4Vector > 0 && c4Counter > MAX_CONTROL_DEV || 
				c4Vector < 0 && c4Counter < -MAX_CONTROL_DEV ) 
			{
				c4Vector *= -1;
			}
			
			if( endVector > 0 && endCounter >  MAX_POINT_DEV || 
				endVector < 0 && endCounter <  -MAX_POINT_DEV ) 
			{
				endVector *= -1;
			}
			
		}
		
	}


	public PspBackground(Context context) {
		super(context);
	
		mSurfaceHolder = getHolder();    
		mSurfaceHolder.addCallback(this);
		
		mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mBgPaint.setColor(getResources().getColor(R.color.bg));
		mBgPaint.setAlpha(50);
		
		mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPointPaint.setColor(Color.YELLOW);
		
		constructCurves();	
	}

	private void constructCurves()
	{	
		mCurves = new Curve[N_CURVES];
		
		mCurvePaint = new Paint[N_CURVES];
		mCurveStroke = new Paint[N_CURVES];
		
		int [] curveColors ={ 	getResources().getColor(R.color.curve2),
								getResources().getColor( R.color.curve1 )};
		
		for(int i=0;i<N_CURVES;i++)
		{
			mCurves[i] =  new Curve();
			
			// setup the fill color
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
			paint.setColor(curveColors[i]);
			mCurvePaint[i] = paint;
			
			// Setup the stroke color
			paint = new Paint(Paint.ANTI_ALIAS_FLAG );
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.WHITE);
			paint.setAlpha(50);
			paint.setStrokeWidth(2);
			
			mCurveStroke[i] = paint;
		}
	
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		mWidth = width;
		mHeight = height;
		
		Bitmap old = mbgBitmap;
		
		LinearGradient g = new LinearGradient(width/2, height, width/2, 0, 
				Color.WHITE,//getResources().getColor(R.color.bg), 
				Color.BLACK,//getResources().getColor(R.color.bg2), 
				TileMode.MIRROR);
		
		//mCurvePaint[1].setShader(g);
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
		mThread = new Thread(this);
		mThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mThread = null;
	}
	
	/** 
	 * Interface to Runnable. Main animation thread using a surface holder
	 */
	public void run() {

		while(mThread != null){
			
			Canvas c = null;
	        try {
	            c = mSurfaceHolder.lockCanvas(null);
	            synchronized (mSurfaceHolder) {
	                drawSin(c);
	            }
	            
	        } finally {
	            if (c != null) {
	                mSurfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
	        
	        try{Thread.sleep(20);}catch(Exception e){}
		}	
	}
	
	public void drawSin(Canvas canvas)
	{
		canvas.drawPaint(mBgPaint);
		
		int h2 = mHeight/2;
		int w2 = mWidth/2;
		
		Path path =null;

		mTicker++;
		
		Curve c = null;
		
		for(int i=0; i< mCurves.length;i++)
		{	
			path = new Path();
			
			c = mCurves[i];
			
			// Point 1
			if(mTicker % c.startSpeed == 0) c.startCounter += c.startVector;
			int p1_y = h2 + c.startCounter, p1_x = -OUT_OF_BOUND; 
		
			
			// CONTROL POINT 1
			c.c1Counter += c.c1Vector;
			int c1_y = h2 + c.c1Counter, c1_x = MAX_CONTROL_X_DEV;
			
			// Mid Point
			if(mTicker % c.midSpeed == 0) c.midCounter += c.midVector;
			int mid_y = h2 + c.midCounter, mid_x = w2;
			
			// CONTROL POINT 2
			int c2_x = w2 - MAX_MID_X_DEV, c2_y = mid_y;
			
			// CONTROL POINT 3
			int c3_x = w2 + MAX_MID_X_DEV, c3_y = mid_y;
			
			// CONTROL POINT 4
			c.c4Counter += c.c4Vector;
			int c4_y = h2 + c.c4Counter, c4_x = mWidth-MAX_CONTROL_X_DEV;
			
			// Point 2
			if(mTicker % c.endSpeed == 0) c.endCounter += c.endVector;
			int pEnd_y = h2 + c.endCounter, pEnd_x = mWidth+OUT_OF_BOUND; 
			
			path.moveTo(p1_x,p1_y);
			//path.cubicTo(c1_x, c1_y, c2_x, c2_y, mid_x, mid_y);
			//path.cubicTo(c3_x, c3_y, c4_x, c4_y, pEnd_x, pEnd_y);
			path.cubicTo(c1_x, c1_y, c2_x, c2_y, pEnd_x, pEnd_y);
			
			path.lineTo(pEnd_x,mHeight+OUT_OF_BOUND);
			path.lineTo(p1_x, mHeight+OUT_OF_BOUND);
			
			// Stroke me 		
			canvas.drawPath(path, mCurvePaint[i]);
			canvas.drawPath(path, mCurveStroke[i]);
			
			if(mShowPoints)
			{
				// Draw the point for debugging
				canvas.drawCircle(p1_x,p1_y,3,mPointPaint);
				canvas.drawCircle(c1_x,c1_y,3,mPointPaint);
				canvas.drawCircle(c2_x,c2_y,3,mPointPaint);
				canvas.drawCircle(mid_x,mid_y,3,mPointPaint);
				canvas.drawCircle(c3_x,c3_y,3,mPointPaint);
				canvas.drawCircle(c4_x,c4_y,3,mPointPaint);
				canvas.drawCircle(pEnd_x,pEnd_y,3,mPointPaint);
			}
			
			c.boundsCheck();
		}
	}
	
}
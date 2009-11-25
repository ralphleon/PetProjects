package com.sonyericsson.android.SampleAnimation;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SinSin extends SurfaceView implements SurfaceHolder.Callback
{	

	private class Star 
	{
		static final int BRIGHT = 100;
		public static final int DELTA = 10;
		
		public Star(int x,int y, int s, int b,int v)
		{
			this.x =x;
			this.y =y;
			this.size =s;
			this.brightness =b;
			this.velocity =v;
		}
		
		/**
		 * @return if the star is dead or not
		 */
		public boolean sparkle()
		{
			brightness += brightnessDelta;
			
			if(brightness > BRIGHT)
			{
				brightnessDelta = -DELTA;
			}
			else if(brightness < 0)
			{
				Log.v("BLAH", "dead");			
				return true;
			}
			
			return false;
		}
		
		public int x =0;
		public int y =0;
		public int size =0;
		public int brightness =0;
		public int velocity =0;
		
		public int brightnessDelta = DELTA;
	}

	
	private final int DELTA = 1;
	private final int N_PARTICLES = 100;
	private float SCALE = 4.0f;
	static final int SIZE = 2;
	private static final int BANDING = 0;
	
	Bitmap mBg,mBgAlpha;

	class SinSinThread extends Thread
	{	
		private boolean mRunning = true;
		private int mAlphaPos = 0;
		private int mDelta = -DELTA;
		
		SurfaceHolder mSurfaceHolder;

		public SinSinThread(SurfaceHolder holder, Context context)
		{
			mSurfaceHolder = holder;
		}
		
		@Override 
		public void run()
		{
			while(mRunning){
				
				Canvas c = null;
	            try {
	                c = mSurfaceHolder.lockCanvas(null);
	                synchronized (mSurfaceHolder) {
	                    draw(c);
	                }
	                
	            } finally {
	                if (c != null) {
	                    mSurfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	            
	            try{sleep(20);}catch(Exception e){}
			}
		}
		
		public void draw(Canvas canvas)
		{
			if(mStars != null)
			{
				canvas.drawColor(Color.BLACK);
				canvas.drawBitmap(mBg, 0, 0, null);
				canvas.drawBitmap(mBgAlpha, mAlphaPos+=mDelta, -10,null);
				
				// We need to reset the alpha bitmap once we've ran out of space
				if(mBgAlpha.getWidth() + mAlphaPos <= mWidth ){
					mDelta = DELTA;
				}
				else if(mAlphaPos >= 0){
					mDelta = -DELTA;
				}
				
				// Draw every star point 
				for(int i=0;i<mStars.length;i++)
				{
					Star star = mStars[i];
					
					// We want to lower the color of every star
					if(star.sparkle()){
						mStars[i] = createStar();
						star = mStars[i];
					}
					
					mPaint.setAlpha(star.brightness);		
					
					canvas.drawCircle(star.x, star.y,star.size, mPaint);
				}
			}
		}

		public void setRunning(boolean b)
		{
			mRunning = b;
		}
	}
	
	private Star [] mStars;
	private Paint mPaint;
	private Random mRandom;
	private int mHeight=0;
	private int mWidth=0;
	
	SinSinThread mThread;

	public SinSin(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        
		mBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        //mBg = Bitmap.createScaledBitmap(bg, (int)(bg.getWidth()/SCALE) , (int)(bg.getHeight()/SCALE), true);
        
        mBgAlpha =  BitmapFactory.decodeResource(context.getResources(), R.drawable.background_alpha);
        //mBgAlpha = Bitmap.createScaledBitmap(bgAlpha, (int)(bgAlpha.getWidth()/SCALE) , (int)(bgAlpha.getHeight()/SCALE), true);
        
        mThread = new SinSinThread(holder,context);
	
    	mRandom = new Random();
    	mRandom.setSeed(System.currentTimeMillis());
    	
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
		mWidth = width;
		mHeight = height;

    	createStars();
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		mThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
	
	private void createStars()
	{
		mStars = new Star[N_PARTICLES];
		
		for(int i=0;i<N_PARTICLES;i++)
		{
			mStars[i] = createStar();
			
			// Randomize the intial brightness
			mStars[i].brightness = mRandom.nextInt(Star.BRIGHT);
			mStars[i].brightnessDelta = (mRandom.nextInt(3) <= 1) ? -Star.DELTA : Star.DELTA;
		}
	}
	
	private Star createStar()
	{
		int x = mRandom.nextInt(mWidth);
		int y = mRandom.nextInt(mHeight - 2*BANDING) + BANDING;
		int s = mRandom.nextInt(SIZE);
		return new Star(x,y,s,0,0);
	}
}
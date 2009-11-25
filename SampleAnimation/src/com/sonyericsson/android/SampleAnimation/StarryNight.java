package com.sonyericsson.android.SampleAnimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sonyericsson.android.SampleAnimation.Starfield.Star;

class StarryNight extends SurfaceView implements SurfaceHolder.Callback
{
	
	class StarryThread extends Thread
	{	
		private boolean mRunning = true;

		protected SurfaceHolder mSurfaceHolder;

		protected int mHeight=0;
		protected int mWidth=0;
		
		public StarryThread(SurfaceHolder holder, Context context, Handler handler)
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

		public void setRunning(boolean b)
		{
			mRunning = b;
		}
		
		public void setSize(int w, int h)
		{
			mWidth = w;
			mHeight = h;
		}
		
		public void draw(Canvas canvas)
		{
			canvas.drawColor(Color.BLACK);
		
			int w = mWidth;
			int h = mHeight;
			
			if(!(w  == 0 || h == 0))
			{	
				Star [] stars = mSky.getStars();
				int n = stars.length;
				
				// Draw random points
				for(int i=n-1; i >= 0;--i)
				{
					Star sprinkle = stars[i];
					mPaint.setAlpha(sprinkle.brightness);
					canvas.drawCircle(sprinkle.x, sprinkle.y, sprinkle.size, mPaint);
					
					sprinkle.y += sprinkle.velocity;
					
				}
			}
			
		}

	}

	private Paint mPaint;
	private Starfield mSky;
	private StarryThread mThread;
	
	public StarryNight(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
        
		holder.addCallback(this);
		
		mSky = new Starfield();
		
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        
        mThread = new StarryThread(holder,context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                //mStatusText.setVisibility(m.getData().getInt("viz"));
                //mStatusText.setText(m.getData().getString("text"));
            }
        });
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{
		mSky.setSize(width,height);		
		mThread.setSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if(mThread !=null)
		mThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		 // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
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
}
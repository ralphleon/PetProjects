package com.sonyericsson.android.SampleAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sonyericsson.android.SampleAnimation.Sprinkle.Node;

class MultiLayerScroll extends SurfaceView implements SurfaceHolder.Callback
{	
	private Paint mBgPaint;

	MultiLayerThread mThread;
	
	Bitmap [] mBitmaps;
	
	int [] mSpeed;
	int [] mXOffset;
	private boolean[] mFlipper;
	private Sprinkle mSprinkle;
	
	class MultiLayerThread extends Thread
	{	
		private static final String TAG = "Multi";

		private boolean mRunning = true;
		
		SurfaceHolder mSurfaceHolder;

		public MultiLayerThread(SurfaceHolder holder, Context context)
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
			Rect clip = canvas.getClipBounds();
			int clipWidth = clip.width();
			
			canvas.drawPaint(mBgPaint);
			
			Bitmap [] bitmaps = mBitmaps;
			int n = bitmaps.length;
			
			for(int i=0;i<n;i++)
			{	
				if(bitmaps[i] != null)
				{
					canvas.save();
					
					// set the offset & flipped state
					mXOffset[i] += mSpeed[i];
					
					if(mXOffset[i] >= clipWidth){
						mXOffset[i] = 0;
						mFlipper[i] = !mFlipper[i];
					}
					
					int offset = mXOffset[i];
					
					if(mFlipper[i]) {
						// draw the first portion	
						canvas.translate(mXOffset[i],0);
						canvas.scale(-1,1);
						canvas.drawBitmap(bitmaps[i],0,0, null);					
						
						canvas.scale(-1,1);
						canvas.drawBitmap(bitmaps[i],0,0, null);
						
					}else{
						
						// draw the first portion
						canvas.scale(-1,1);
						canvas.drawBitmap(bitmaps[i],-offset-clipWidth,0, null);					
						
						canvas.scale(-1, 1);
						canvas.drawBitmap(bitmaps[i],offset - clipWidth,0, null);			
					}
					
					canvas.restore();	
				}
			}
			
			Node [] sprinkle = mSprinkle.getNodes();
			
			if(sprinkle != null){
				// Draw every star point 
				for(int i=0;i<sprinkle.length;i++)
				{					
					sprinkle[i].sparkle();
					sprinkle[i].draw(canvas);	
				}
			}else{
				Log.e("Multi","Dead sprinkles!");
			}
		}

		public void setRunning(boolean b)
		{
			mRunning = b;
		}
	}

	public MultiLayerScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        mBgPaint = new Paint();
        mBgPaint.setColor(0xFF000000);
          
        int width = 854;
        int height = 480;
        
        mSprinkle = new Sprinkle();
  
        mThread = new MultiLayerThread(holder,context);     
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
    	mSprinkle.setSize(width,height);
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

	public void setLayers(Bitmap[] bitmaps, int[] speed) {
		
		mBitmaps = bitmaps;
		mSpeed = speed;
		
		mXOffset = new int[mBitmaps.length];
		mFlipper = new boolean[mBitmaps.length];
		
		for(int i=0;i<mBitmaps.length;i++){
			mXOffset[i] = 0;
			mFlipper[i] = false;
		}        
	}
}
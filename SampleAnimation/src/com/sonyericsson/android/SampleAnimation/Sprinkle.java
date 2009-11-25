package com.sonyericsson.android.SampleAnimation;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

class Sprinkle 
{
	private static final int N_PARTICLES = 100;
	static final int BRIGHT = 150;
	public static final int DELTA = 25;
	static final int SIZE = 2;
	private static double SPREAD = 50;
	
	Random mRandom;
	Node [] mNodes;
	private int mWidth = 0;
	private int mHeight = 0;
	
	private int mBanding = 0;
	
	private Paint mStarPaint;
	
	public Sprinkle(){
		
		mRandom = new Random();
		
		mStarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mStarPaint.setColor(Color.WHITE);
	}
	
	public void setSize(int w, int h){
		mWidth = w;
		mHeight = h;
		mBanding = h/2;
		createStars();
	}
	
	public void setBanding(int b)
	{
		mBanding = b;
	}
	
	public Node [] getNodes() {
		
		return mNodes;
	}
	
	private void createStars()
	{
		mNodes = new Node[N_PARTICLES];
		
		for(int i=0;i<N_PARTICLES;i++)
		{
			mNodes[i] = new Node();
			
			// Randomize the intial brightness
			mNodes[i].brightness = mRandom.nextInt(Sprinkle.BRIGHT);
			mNodes[i].brightnessDelta = (mRandom.nextInt(3) <= 1) ? -Sprinkle.DELTA : Sprinkle.DELTA;
		}
	}
	
	public class Node{

		public Node(int x,int y, int s, int b,int v)
		{
			this.x =x;
			this.y =y;
			this.size =s;
			this.brightness =b;
			this.velocity =v;
		}
		
		public Node()
		{
			randomize();
		}
		public void randomize()
		{
			brightness = 0;
			brightnessDelta = DELTA;
			
			x = mRandom.nextInt(mWidth);
			double g = mRandom.nextGaussian();	
			y = mBanding + (int)(SPREAD*g);
			size = mRandom.nextInt(SIZE);			
		}
		
		/**
		 * @return if the star is dead or not
		 */
		public void sparkle()
		{
			brightness += brightnessDelta;
			
			if(brightness > BRIGHT)
			{
				brightnessDelta = -DELTA;
			}
			else if(brightness < 0)
			{
				randomize();	
			}
		}
		
		public void draw(Canvas canvas)
		{
			mStarPaint.setAlpha(brightness);
			canvas.drawCircle(x, y, size, mStarPaint);
		}
		
		public int x =0;
		public int y =0;
		public int size =0;
		public int brightness =0;
		public int velocity =0;
		
		public int brightnessDelta = DELTA;
	}
}
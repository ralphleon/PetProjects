package com.sonyericsson.android.SampleAnimation;

import java.util.Random;

public class Starfield
{	
	static final int SIZE = 3;
	private static final int BRIGHT = 200;
	static final int STEP =4;
	static final int NUM_STARS = 100;
	
	private Star [] mStars;

	private Random mRandom;
	private int mWidth;
	private int mHeight;

	public class Star 
	{
		public Star(int x,int y, int s, int b,int v)
		{
			this.x =x;
			this.y =y;
			this.size =s;
			this.brightness =b;
			this.velocity =v;
		}
		
		public void clone(Star lhs)
		{
			this.x = lhs.x;
			this.y = lhs.y;
			this.size = lhs.size;
			this.brightness = lhs.brightness;
			this.velocity = lhs.velocity;
			this.brightnessDelta = lhs.brightnessDelta;
		}
		
		public void sparkle()
		{
			brightness += brightnessDelta;
		
		}
		
		public void go()
		{
			y += velocity;
			
			if( y > mHeight){
				clone(createStar());
				y=0;
			}
		}
		
		public int x =0;
		public int y =0;
		public int size =0;
		public int brightness =0;
		public int velocity =0;
		public int brightnessDelta = 1;
		
	}
	
	public Starfield()
	{
		mRandom = new Random();
	}

	public void setSize(int w, int h)
	{
		mWidth = w;
		mHeight = h;
		
		createStars();
	}

	private void createStars()
	{
		mStars = new Star[NUM_STARS];
		
		for(int i=0;i<NUM_STARS;i++)
		{
			mStars[i] = createStar();
		}
	}
	
	private Star createStar()
	{
		int x = mRandom.nextInt(mWidth);
		int y = mRandom.nextInt(mHeight);
		int s = mRandom.nextInt(SIZE);
		int b = mRandom.nextInt(BRIGHT);
		int v = mRandom.nextInt(STEP)+1;
		return new Star(x,y,s,b,v);
	}
	
	public Star [] getStars(){ return mStars;}
	
}
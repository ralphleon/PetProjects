package com.sonyericsson.android.SampleAnimation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SampleAnimation extends Activity
{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
    	// Load the bitmaps for multi layer scroll;
    	int [] ids = { R.drawable.shape1, R.drawable.shape2, R.drawable.shape3,R.drawable.shape4};
    	//int [] ids = { R.drawable.test1, R.drawable.test2};
        
    	int [] speed = {2,3,4,5};
        
        Bitmap [] bitmaps = new Bitmap[ids.length];
        
        for(int i=0;i<ids.length;i++)
        {
        	try{
        		Bitmap b = BitmapFactory.decodeResource(getResources(),ids[i]);
        		bitmaps[i] = Bitmap.createScaledBitmap(b, 854, 480, true); 
        	}catch(OutOfMemoryError e){
        		Log.e("Multi","Out of memory on initial create");
        	}
        }
    	
    	// No title & Fullscreen 
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
    	super.onCreate(savedInstanceState);
    
    	setContentView(new PspBackground(getBaseContext()));
  
        int index = getIntent().getExtras().getInt("Type");
    	View v = null;
    	
        switch(index){
        case AnimationActivity.STARRY:
        	v = new StarryNight(getBaseContext(),null);
        	break;
        case AnimationActivity.ZSYS:
        	v =new SinSin(getBaseContext(),null);
        	break;
        case AnimationActivity.PSP:
        	v = new PspBackground(getBaseContext());
            break;
        case AnimationActivity.MULTI:
        	MultiLayerScroll ml = new MultiLayerScroll(getBaseContext(),null);
            ml.setLayers(bitmaps,speed);
        	v = ml;
        	break;
        }
        
        setContentView(v);
    }
    
}
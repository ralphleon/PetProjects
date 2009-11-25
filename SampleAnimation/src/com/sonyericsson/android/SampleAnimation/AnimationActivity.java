package com.sonyericsson.android.SampleAnimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AnimationActivity extends Activity implements OnItemClickListener
{   
	public final static int MULTI = 0;
	public final static int PSP = 1;
	public final static int ZSYS = 2, STARRY = 3;
	
    private final static String TAG = AnimationActivity.class.getSimpleName(); 
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  	
        // Create the list
        String[] mStrings = new String[]{"Multi-Layer Scroll", "PSP Background","Sprinkles","Starry Night"}; 

        ListView listView = (ListView)findViewById(R.id.menuList);
        
        // Create an ArrayAdapter, that will actually make the Strings above appear in the ListView 
        listView.setAdapter(new ArrayAdapter<String>(this, 
                         android.R.layout.simple_list_item_1, mStrings));
        
        listView.setOnItemClickListener(this);
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		Intent i = new Intent(this,SampleAnimation.class);
		i.putExtra("Type",arg2);		
		startActivity(i);
	}
}
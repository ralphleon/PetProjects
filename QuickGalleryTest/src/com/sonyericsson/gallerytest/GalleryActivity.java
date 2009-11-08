package com.sonyericsson.gallerytest;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Gallery g = (Gallery) findViewById(R.id.gallery1);
        g.setAdapter(new ImageAdapter(this));
        g.setClipChildren(false);
        g.setAnimationCacheEnabled(true);
        g.setClipToPadding(false);
        
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(GalleryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        
        g.setSelection(3);
        
        g = (Gallery) findViewById(R.id.gallery2);
        g.setAdapter(new ImageAdapter(this));
        g.setClipChildren(false);
        g.setAnimationCacheEnabled(true);
        g.setClipToPadding(false);
        
        g.setSelection(3);
        
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(GalleryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public void myClickHandler(View target) {
       Gallery g = (Gallery) findViewById(R.id.gallery1);
       Gallery g2 = (Gallery) findViewById(R.id.gallery2);
       
       g.startLayoutAnimation();
       g2.startLayoutAnimation();
       
    }
    
    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        private Integer[] mImageIds = {
                R.drawable.sample1,
                R.drawable.sample2,
                R.drawable.sample3,
                R.drawable.sample4,
                R.drawable.sample5,
                R.drawable.sample6,
                R.drawable.sample7
        };

        public ImageAdapter(Context c) {
            mContext = c;
            TypedArray a = obtainStyledAttributes(R.styleable.Gallery1); 
                mGalleryItemBackground = a.getResourceId( 
                    R.styleable.Gallery1_android_galleryItemBackground, 0); 
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            i.setImageResource(mImageIds[position]);
            
            //i.setLayoutParams(new Gallery.LayoutParams(100, 100));
            
            i.setLayoutParams(new Gallery.LayoutParams(150, 150));
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            i.setBackgroundResource(mGalleryItemBackground);

            return i;
        }
    }
}
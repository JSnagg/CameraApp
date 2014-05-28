package com.example.cameraapp;

import java.io.ByteArrayOutputStream;
import com.google.android.gms.maps.SupportMapFragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.cameraapp.SimpleGestureFilter.SimpleGestureListener;
import com.example.cameraapp.R;
import com.example.cameraapp.MainActivity;
import com.example.cameraapp.CameraPreview;
import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements SimpleGestureListener{
		PlaceholderFragment placeHolder = new PlaceholderFragment();
		PlaceholderFragment2 placeHolder2 = new PlaceholderFragment2();
		PlaceholderFragment3 placeHolder3 = new PlaceholderFragment3();
		int pos = 0;
		int view = 0;
		int TAKE_PICTURE = 1;
		static Uri imageUri;
		static Bitmap bitmap;
		
        private SimpleGestureFilter detector;
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                // Detect touched area
                detector = new SimpleGestureFilter(this,this);
                
        		if (savedInstanceState == null) {
        			view = 1;
        			getSupportFragmentManager().beginTransaction().add(R.id.container, placeHolder).commit();
        			placeHolder.setContextMethod(MainActivity.this);
        			placeHolder2.setContextMethod(MainActivity.this);
        			placeHolder3.setContextMethod(MainActivity.this);
        		}
        }
          
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    
    @Override
     public void onSwipe(int direction) {
      //String str = "";
      
      switch (direction) {
      
      case SimpleGestureFilter.SWIPE_RIGHT : 
    	  			view = 1;
    	  			getSupportFragmentManager().beginTransaction().hide(placeHolder).commit();
    	  			getSupportFragmentManager().beginTransaction().show(placeHolder3).commit();
      				break;
      				
      case SimpleGestureFilter.SWIPE_LEFT :  
    	  			view = 2;
    	  			getSupportFragmentManager().beginTransaction().hide(placeHolder).commit();
    	  			if(pos == 0) getSupportFragmentManager().beginTransaction().add(R.id.container, placeHolder3).commit();
    	  			else if (pos == 1) getSupportFragmentManager().beginTransaction().show(placeHolder3).commit();
    	  			pos = 1;
      				break;
      				
      case SimpleGestureFilter.SWIPE_DOWN :  break;
      case SimpleGestureFilter.SWIPE_UP :  break;
      
      
      }
     }
      
     @Override
     public void onDoubleTap() {
    	if(view == 1) Toast.makeText(this, "Capture Photo", Toast.LENGTH_SHORT).show();
    	else if(view == 2) Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show();
     }
     
     
 	public static class PlaceholderFragment extends Fragment {

		MainActivity context;
		View rootView;
		
		// Camera stuff
		private Camera mCamera;
		private CameraPreview mPreview;
		
		public PlaceholderFragment()
		{
		}
		
		public void setContextMethod(MainActivity context)
		{
			this.context = context;
		}
		

	    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
	    Button button;
	    ImageView imageView;

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {

	        final View rootView = inflater.inflate(R.layout.fragment1,container, false);
	        
			/*// Create an instance of Camera
	        mCamera = getCameraInstance();
	        // Create our Preview view and set it as the content of our activity.
	        mPreview = new CameraPreview(context, mCamera);
	        FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
	        preview.addView(mPreview);*/
	        
	        button = (Button) rootView.findViewById(R.id.button_camera);
	        //imageView = (ImageView) rootView.findViewById(R.id.image);

	        button.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View view) {

	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                startActivityForResult(intent,
	                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

	            }
	        });

	        return rootView;

	    }

	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	            if (resultCode == Activity.RESULT_OK) {

	                Bitmap bmp = (Bitmap) data.getExtras().get("data");
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();

	                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                byte[] byteArray = stream.toByteArray();

	                // Convert ByteArray to Bitmap::

	                bitmap = BitmapFactory.decodeByteArray(byteArray, 0,byteArray.length);

	               //imageView.setImageBitmap(bitmap);

	            }
	        }        
	    }    
	    
		public static Camera getCameraInstance()
		{
		    Camera c = null;
		    try {
		        c = Camera.open();
		    }
		    catch (Exception e){
		    }
		    return c; 
		}

	}
 	
 
 	public static class PlaceholderFragment2 extends Fragment {
		MainActivity context;
		View rootView;


		public PlaceholderFragment2()
		{
		}
		
		
		public void setContextMethod(MainActivity context)
		{
			this.context = context;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
		{
			rootView = inflater.inflate(R.layout.fragment2,container, false);
			ImageView imageView;
			imageView = (ImageView) rootView.findViewById(R.id.image);
			imageView.setImageBitmap(bitmap);
			return rootView;
		}
		
	}      
 	
 	public static class PlaceholderFragment3 extends Fragment {
		MainActivity context;
		View rootView;


		public PlaceholderFragment3()
		{
		}
		
		
		public void setContextMethod(MainActivity context)
		{
			this.context = context;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
		{
			rootView = inflater.inflate(R.layout.fragment3,container, false);
			return rootView;
		}
		
	}  
 }
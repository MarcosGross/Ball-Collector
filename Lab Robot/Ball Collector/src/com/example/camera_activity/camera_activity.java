package com.example.camera_activity;
 
 import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.lang.Object;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.*;
import org.opencv.core.*;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;






import com.example.ball3.R;
 public class camera_activity extends Activity implements CvCameraViewListener2 {  
   

   int send;
   private Mat mRgba;  
   private Mat mHSV;
   private Mat threshold;
   private boolean timer;
   private List<Ball> blueballs;
   private List<Ball> wallballs;
   private List<Ball> selected;
   private int centerx = 320;
   private int centery = 400;
   private int separationdistance=15;
   private CountDownTimer timer3;
   Bitmap bitmap;
   int x_center;
   int y_center;
   int points;
   
      
   
  	private BluetoothAdapter mBluetoothAdapter = null;
  	private BluetoothSocket btSocket = null;
  	private OutputStream outStream = null;
  	private static String address;
  	private static final UUID MY_UUID = UUID
  			.fromString("00001101-0000-1000-8000-00805F9B34FB");
  	Handler handler = new Handler();
  	boolean stopWorker = false;
  	Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      
      
      
      
      
      

   private CameraBridgeViewBase  mOpenCvCameraView;  
   private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {  
     @Override  
     public void onManagerConnected(int status) {  
       switch (status) {  
         case LoaderCallbackInterface.SUCCESS: {  
           mOpenCvCameraView.enableView();  
         } break;  
         default:{  
           super.onManagerConnected(status);  
         } break;  
       }  
     }  
   };  
   
   public camera_activity() {  
    
   }  
   
   
   @Override  
   public void onCreate(Bundle savedInstanceState) {  
    
     super.onCreate(savedInstanceState);  
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
     setContentView(R.layout.surface_view);  
     mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial2_activity_surface_view);  
     mOpenCvCameraView.setMaxFrameSize(640, 480);
     mOpenCvCameraView.setCvCameraViewListener(this);
     mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
     
     Bundle extras = getIntent().getExtras();
     if (extras != null) {
         address = extras.getString("address");
         Connect();
     }
     timer3 = new CountDownTimer(60000, 1000) { // adjust the milli seconds here

         public void onTick(long millisUntilFinished) {  
        	 timer=true;
         }

         public void onFinish() {
             timer=false;
             writeData("x"+222+"r");
             writeData("x"+222+"r");
             writeData("x"+222+"r");
         }
      }.start();
   }  
   
   
  
   @Override  
   public void onPause(){
	   
     super.onPause();  
     if (mOpenCvCameraView != null)  
       mOpenCvCameraView.disableView();  
   }  
   
   @Override  
   public void onResume(){
	   
     super.onResume();  
     OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);  
   }  
   
   
   
   @Override
public void onDestroy() {    
     try{
    	writeData("x"+300);
     btSocket.close();
     }catch(IOException e){}
    	 
     
     if (mOpenCvCameraView != null) { 
       mOpenCvCameraView.disableView();
     }
     super.onDestroy(); 
   }  
   
   
   
   @Override
public void onCameraViewStarted(int width, int height) {  
     mRgba = new Mat(height, width, CvType.CV_8UC4);  
     
   }  
   @Override
public void onCameraViewStopped() {  
     mRgba.release();  
    
   }  
   @Override
public Mat onCameraFrame(CvCameraViewFrame inputFrame) {   
        mRgba = inputFrame.rgba();  
        
        mHSV = new Mat();
        threshold=new Mat();
        
        Imgproc.cvtColor(mRgba, mHSV, Imgproc.COLOR_RGB2HSV);
        
        if(timer)
        { 	 blueballs = new ArrayList<Ball>();
	        if(detectBlueballs())
	        {

				Ball selectedball = null;
				double distance = 99999999;
				
					if(blueballs.size()>0)
					{
						for (int i = 0; i < blueballs.size(); i++) {
							Ball theBall = blueballs.get(i);
							
							double tempdistance = Math.sqrt(Math.pow(theBall.getXPos()-centerx,2 )+ Math.pow(theBall.getYPos()- centery,2));
							if(tempdistance<distance)
							{
								distance=tempdistance;
								selectedball=theBall;
							}
						}
					}
					if(selectedball!=null)
			        {
						String data = "x" + selectedball.getXPos()+ "f";
						Core.putText(mRgba, data ,new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
			        	writeData(data);//1 is for the direction-
			        }
					selected = new ArrayList<Ball>();
					selected.add(selectedball);
	        	drawObject(selected, mRgba);
	        	selected.clear();
	        }
	        else{
	        	Core.putText(mRgba, "No balls found",new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
	        	writeData("x" +100+ "f");//if no ball is found rotate left
	        }
	       
	       
        }
        else
        {
        	wallballs = new ArrayList<Ball>();
        	if(detectwallballs())
        	{
        		Ball leftball = null;
				int distanceleft=999;
				Ball rightball = null;
				int distanceright=999;
				double tempdist;
					if(wallballs.size()>0)
					{drawObject(wallballs, mRgba);
						for (int i = 0; i < wallballs.size(); i++) {
							Ball theBall = wallballs.get(i);
							tempdist=theBall.getXPos()-320;
							if(tempdist>0)
							{
								if(tempdist<distanceright)
								{
									distanceright= (int) tempdist;
									rightball=theBall;
								}
							}
							else{
								if(tempdist*-1<distanceleft)
								{
									distanceleft=(int) tempdist;
									leftball=theBall;
								}
									
							}
						}
					
					if(leftball!=null&rightball!=null)
					{
					if(leftball.getYPos()>rightball.getYPos()+5)
					{
						//turn right
						String data = "x"+100+"f";
						Core.putText(mRgba, data ,new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
						writeData(data);
						writeData(data);
					}
					else if (leftball.getYPos()<rightball.getYPos()-5)
					{	String data = "x"+400+"f";
						Core.putText(mRgba, data ,new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
						writeData(data);
						writeData(data);
					}
					else
					{	int pixelseparation=rightball.getXPos()-leftball.getXPos();
						Core.putText(mRgba, "Separation = "+pixelseparation,new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
						if(pixelseparation< separationdistance)
						{
							String data = "x"+300+"f";
							Core.putText(mRgba, data ,new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
							writeData(data);
							writeData(data);
						}
						else
						{
							String data = "x"+1+"d";
							Core.putText(mRgba, data ,new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 3);
							writeData(data);
							writeData(data);
							writeData(data);
							timer3.start();
						}
						
					}
					}
					}
        	}
        	else
        	{
        		writeData("x" + 100 + "f");//if no wall is found rotate left
        	}
        	
        }
		mHSV.release();
            return mRgba;    
              }

private boolean detectBlueballs(){
	Ball blueBall = new Ball(Ball.Colours.BLUE);
	Core.inRange(mHSV, blueBall.getHsvMin(), blueBall.getHsvMax(),threshold);
	morphOps();
	
	Mat temp = new Mat();
	threshold.copyTo(temp);
	
	// The two variables below are the return of "findContours" processing.
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	Mat hierarchy = new Mat();
	
	// find contours of filtered image using openCV findContours function		
	Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
	
	if (contours.size() > 0) {
		int numObjects = contours.size();

		//if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
		if (numObjects < 10) {
			
			for (int i=0; i< contours.size(); i++){
				Moments moment = Imgproc.moments(contours.get(i));
				
				double area = moment.get_m00();

				if (area > 40*40) {
					Ball ball = new Ball();
					ball.setXPos((int)(moment.get_m10() / area));
					ball.setYPos((int)(moment.get_m01() / area));
					
					if (blueBall != null){
						ball.setType(blueBall.getType());
						ball.setColour(blueBall.getColour());
					}
					blueballs.add(ball);					
					
				}
			}
			
			if(blueballs.size()>0)
			{
				temp.release();
				return true;
			}

		} else {
			Core.putText(mRgba, "TOO MUCH NOISE! ADJUST FILTER", new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 2);
			temp.release();
			return false;
		}

	}
	temp.release();
	return false;
}

private boolean detectwallballs(){
	Ball wallBall = new Ball(Ball.Colours.GREEN);
	Core.inRange(mHSV, wallBall.getHsvMin(), wallBall.getHsvMax(),threshold);
	morphOps();
	
	Mat temp = new Mat();
	threshold.copyTo(temp);
	
	// The two variables below are the return of "findContours" processing.
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	Mat hierarchy = new Mat();
	
	// find contours of filtered image using openCV findContours function		
	Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
	
	if (contours.size() > 0) {
		int numObjects = contours.size();

		//if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
		if (numObjects < 10) {
			
			for (int i=0; i< contours.size(); i++){
				Moments moment = Imgproc.moments(contours.get(i));
				
				double area = moment.get_m00();

				if (area > 10*10) {
					Ball ball = new Ball();
					ball.setXPos((int)(moment.get_m10() / area));
					ball.setYPos((int)(moment.get_m01() / area));
					
					if (wallBall != null){
						ball.setType(wallBall.getType());
						ball.setColour(wallBall.getColour());
					}
					wallballs.add(ball);					
					
				}
			}
			
			if(wallballs.size()>0)
			{
				temp.release();
				return true;
			}

		} else {
			Core.putText(mRgba, "TOO MUCH NOISE! ADJUST FILTER", new Point(0, 50), 1, 2, new Scalar(0, 0, 255), 2);
			temp.release();
			return false;
		}

	}
	temp.release();
	return false;
}

private void drawObject(List<Ball> theBalls, Mat frame) {

			for (int i = 0; i < theBalls.size(); i++) {
				Ball theBall = theBalls.get(i);

				Core.circle(frame, new Point(theBall.getXPos(), theBall.getYPos()), 10, new Scalar(0, 0, 255));
				Core.putText(frame, theBall.getXPos() + " , " + theBall.getYPos(), new Point(theBall.getXPos(), theBall.getYPos() + 20)
						, 1, 1, new Scalar(0, 255, 0));
				Core.putText(frame, theBall.getType().toString(), new Point(theBall.getXPos(),
						theBall.getYPos() - 30), 1, 2, theBall.getColour());
			}

		}
		
	
private void morphOps() {
	
	Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
	Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(8,8));
	
	Imgproc.erode(threshold, threshold, erodeElement);
	Imgproc.dilate(threshold, threshold, dilateElement);
}


   
   
   
   public void Connect() {
	   if(mBluetoothAdapter.isEnabled()){
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		
			Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();
			
			mBluetoothAdapter.cancelDiscovery();
			try {
				btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
				btSocket.connect();
				
				Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
			
			} catch (IOException e) {
				try {
					btSocket.close();
				
				} catch (IOException e2) {
					
					Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
					
				}
	
			}
	   }
	   else {
		   Toast.makeText(getApplicationContext(), "Bluetooth is disabled", Toast.LENGTH_SHORT).show();	   
	   }
	 
	}



private void writeData(String data) {

	   if(mBluetoothAdapter.isEnabled()){
			try {
				outStream = btSocket.getOutputStream();
			} catch (IOException e) {
				
			}
			try {
				outStream.write(data.getBytes());
			} catch (IOException e) {
				
			}
	   }
	  
	}


   
   
 }  
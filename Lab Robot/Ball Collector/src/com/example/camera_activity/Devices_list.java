package com.example.camera_activity;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ball3.R;
  
public class Devices_list extends Activity {
  

	

    ListView pairedListView;
    CheckBox checkBox;
    Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
  
    

       
    
       
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_layout);
         
      
       
         

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.devices_name_layout);

        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
         
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBt();
   
       
      mPairedDevicesArrayAdapter.clear();
 

       

      
  

      Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
      

      if (pairedDevices.size() > 0) {
          for (BluetoothDevice device : pairedDevices) {
              mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
          }
      } else {
          mPairedDevicesArrayAdapter.add("no devices paired");
      }
  }
  

    private void CheckBt() {
	       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	         if (!mBluetoothAdapter.isEnabled()) {
	                   startActivity(i);
	           }

	           if (mBluetoothAdapter == null) {
	               
	           }
		   }
    
    
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener(){
    	
        @Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3){
        	
    
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
  
            Connect(address);
            
            
            
            
            
            
        }
        
    };
    
    public void Connect(String address){
    	
    	Intent i = new Intent(getApplicationContext(), camera_activity.class);
    	i.putExtra("address", address);
    	startActivity(i);
    
 	  
 	  
    }
    
   
}

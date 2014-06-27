package com.example;

import com.example.bgservice.MQTTService;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class MqttClientApplication extends Application {

	
	@Override
	public void onCreate() {
         
		new StartMqttClientServiceThread().start(); 
         
		super.onCreate(); 
	}
	
	public class StartMqttClientServiceThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			startMqttClientService();
		}
	}
	
	private void startMqttClientService() {
		Log.i("test", "启动mqtt服务");
		final Intent intent = new Intent(this, MQTTService.class);
		startService(intent);
	}
	
}
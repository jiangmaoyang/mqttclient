package com.example.bgservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTService extends Service {

	public static String BROKER_URL = "tcp://192.10.10.123:1883";
	public static final String clientId = "CAPTURE_PUSH";

	public static final String TOPIC = "CAPTURE";
	private MqttClient mqttClient;
	private Logger log;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {

		Log.i("test","启动推送service");
		
		log = LoggerFactory.getLogger(MQTTService.class);

		log.info("启动推送service");
		
		log.info("推送地址"+BROKER_URL);
		
		try {

			mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
	
			mqttClient.setCallback(new PushCallback(this));
		} catch (MqttException e) {
			stopSelf();
			log.error("onStart mqtt service error "+e.getMessage());
			e.printStackTrace();
		}
		
		new Thread(new Runnable(){
			public void run() {
				try {
					log.info("连接");
					mqttClient.connect();
					log.info("订阅");
					mqttClient.subscribe(TOPIC);

				} catch (MqttException e) {
					stopSelf();
					log.error("onStart mqtt service error "+e.getMessage());
					e.printStackTrace();
				}

			}
		}).start();

		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		try {
			log.info("onDestroy mqttClient.disconnect");
			if(mqttClient!=null&&mqttClient.isConnected())
			mqttClient.disconnect(0);
			stopSelf();
		} catch (MqttException e) {
			stopSelf();
			log.error("onDestroy mqtt service error "+e.getMessage());
			e.printStackTrace();
		}
	}
	
}

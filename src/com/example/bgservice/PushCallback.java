package com.example.bgservice;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.mqtt_android_client.MainActivity;
import com.example.mqtt_android_client.R;
import com.example.util.AndroidUtils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushCallback implements MqttCallback {

	private Context context;
	NotificationManager mNotifMan;
	private Logger log;
	
	public PushCallback(Context context) {

		log = LoggerFactory.getLogger(MQTTService.class);
		this.context = context;
	}

	@Override
	public void connectionLost(Throwable cause) {
		// We should reconnect here
		log.info("跟mq 连接断开关闭后台服务 ");
		stopBlackIceService();
		log.info("跟mq 连接断开原因 " +cause.getMessage());
		boolean b = AndroidUtils.serviceIsRunning(context, AndroidUtils.SERVICE_NAME);
		log.info("跟mq 连接断开 ，重新启动 service "+b);
		Log.i("test", "跟mq 连接断开关闭后台服务 跟mq 连接断开 ，重新启动 service");
		startBlackIceService();
		
	}
	
	private void startBlackIceService() {
		final Intent intent = new Intent(context, MQTTService.class);
		context.startService(intent);
	}
	
	private void stopBlackIceService() {

		final Intent intent = new Intent(context, MQTTService.class);
		context.stopService(intent);
	}
	
	@Override
	public void messageArrived(MqttTopic topic, MqttMessage message)
			throws Exception {
		String text = new String(message.getPayload());
		
		log.info("接受到推送信息" + text);
		Log.i("test", "接受到推送信息" + text);
		
		showNotification(text);

	}

	@Override
	public void deliveryComplete(MqttDeliveryToken token) {
	}

	@SuppressLint("NewApi")
	private void showNotification(String text) {
		 mNotifMan = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification n = new Notification();
//
//		n.flags |= Notification.FLAG_SHOW_LIGHTS;
//		n.flags |= Notification.FLAG_AUTO_CANCEL;
		String name = context.getResources().getText(R.string.app_name).toString();
//		n.tickerText = name;
//
//		n.defaults = Notification.DEFAULT_ALL;
//
//		n.icon = R.drawable.ic_launcher;
//		n.when = System.currentTimeMillis();

		Notification n = null;

		if (text != null && !"".equals(text)) {
	    		
			//打开自定义的Activity
        	Intent i = new Intent(context, MainActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	PendingIntent pi = PendingIntent.getActivity(context, 0,
					  i, 0);
//			n.setLatestEventInfo(context, name, text, pi);
//			mNotifMan.notify(0, n);
				

	            Builder builder = new Notification.Builder(context);
	            builder.setContentIntent(pi)
	                    .setSmallIcon(R.drawable.ic_launcher)
	                    .setWhen(System.currentTimeMillis()).setAutoCancel(false)
	                    .setContentTitle(name).setContentText(text);
	            n = builder.build();
	            n.defaults =Notification.DEFAULT_SOUND;//设置为默认的声音
				mNotifMan.notify(0, n);

		}

	}
	


}

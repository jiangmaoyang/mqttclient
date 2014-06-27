package com.example.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class AndroidUtils {
	

	public static final String SERVICE_NAME = "com.example.bgservice.MQTTService";
    
    public static boolean serviceIsRunning(Context context,String serviceName) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				Log.e("test", "推送服务后台已经在运行");
				return true;
			}
		}
		return false;
	}
}

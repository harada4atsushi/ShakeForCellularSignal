package com.aharada.shakeforcellularsignal;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 端末が振られた動きを感知するイベントリスナークラス
 * @author a.harada
 */
public class ShakeListener implements SensorEventListener {

	// センサーマネージャ
	private SensorManager mSensorManager;
	
	// リスナークラス
	private IOnShakeListener iOnShakeListener;
	
	// 加速度センサー前回の値
	private float[] oldValues = new float[3];

	// 加速度センサー取得値
	private float[] newValues = new float[3];
	
	private long mPreTime;
	
	private long mPreTime2;

	private int shakeCount = 0;
	
	/**
	 * コンストラクタ　
	 * 加速度センサーのリスニングを開始する。
	 * @param context Activityから受け取ったContext
	 */
	public ShakeListener(Context context) {
		mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    	List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if (list.size() > 0) {
    		mSensorManager.registerListener(this, list.get(0), SensorManager.SENSOR_DELAY_UI);
    	}
	}

	/**
	 * ShakeListenerをセットする
	 * @param listener
	 */
	public void setOnShakeListener(IOnShakeListener listener) {
		iOnShakeListener = listener;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// 加速度センサー以外は無視
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}
		long curTime = System.currentTimeMillis();
		long diffTime = curTime - mPreTime;
		long diffTime2 = curTime - mPreTime2;
				
		// 0.1秒ごとに処理
		if (diffTime > 100) {
			newValues[0] = event.values[SensorManager.DATA_X];
			newValues[1] = event.values[SensorManager.DATA_Y];
			newValues[2] = event.values[SensorManager.DATA_Z];
			
			// どれくらい端末が移動したかを算出する（単位:m/s）
			float speed = (Math.abs(newValues[0] - oldValues[0])
					+ Math.abs(newValues[1] - oldValues[1])
					+ Math.abs(newValues[2] - oldValues[2])) / diffTime * 1000;
		
			// 速度が100m/s以上の場合
			if (speed > 100) {
				shakeCount++;
			}
			
			// 1秒ごとに処理
			if (diffTime2 > 1000) {
				if (iOnShakeListener != null) {
					// onSwingメソッドを呼び出す
					iOnShakeListener.onShake(shakeCount);
					shakeCount = 0;
				}
				mPreTime2 = curTime;
			}
				
			oldValues[0] = newValues[0];
			oldValues[1] = newValues[1];
			oldValues[2] = newValues[2];
			mPreTime = curTime;
		}
	}

}

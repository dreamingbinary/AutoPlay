/*
 * Copyright 2014 Luuk Willemsen (Twinone)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.twinone.autoplay;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class DialogService extends Service {

	private static final String TAG = "DialogService";
	private AlertDialog mDialog;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private BroadcastReceiver mReceiver;

	private class JackReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Received broadcast");
			if (isInitialStickyBroadcast())
				return;
			if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
				int state = intent.getIntExtra("state", 0);
				if (state == 1) {
					onJackPluggedIn();
				} else {
					onJackPluggedOut();
				}
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		filter.addCategory("default");
		mReceiver = new JackReceiver();
		registerReceiver(mReceiver, filter);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		return START_STICKY;
	}

	private void onJackPluggedIn() {
		final SharedPreferences sp = getSharedPreferences(
				SettingsFragment.FILENAME, Context.MODE_PRIVATE);
		final String mode = sp.getString(getString(R.string.pref_key_mode),
				getString(R.string.pref_def_mode));

		// Don't play music if calling
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		int state = tm.getCallState();
		if (state != TelephonyManager.CALL_STATE_IDLE) {
			Log.d(TAG, "Phone was calling, not starting music / showing dialog");
			return;
		}
		if (mode.equals(getString(R.string.pref_val_mode_directly))) {
			Log.d(TAG, "a");
			startExternalPlayer();
		} else {
			Log.d(TAG, "b");
			showDialog();
		}

	}

	private void onJackPluggedOut() {
		if (mDialog != null) {
			mDialog.cancel();
		}
	}

	private void showDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(R.string.plugin_dlg_tit);
		ab.setMessage(R.string.plugin_dlg_msg);
		ab.setPositiveButton(android.R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				startExternalPlayer();
			}
		});
		ab.setNegativeButton(android.R.string.cancel, null);
		ab.setCancelable(false);

		mDialog = ab.create();
		mDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
		mDialog.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		mDialog.show();
	}

	private void startExternalPlayer() {
		long time = SystemClock.uptimeMillis();
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent down = new KeyEvent(time, time, KeyEvent.ACTION_DOWN,
				KeyEvent.KEYCODE_MEDIA_PLAY, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, down);
		time++;
		Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		KeyEvent up = new KeyEvent(time, time, KeyEvent.ACTION_UP,
				KeyEvent.KEYCODE_MEDIA_PLAY, 0);
		upIntent.putExtra(Intent.EXTRA_KEY_EVENT, up);

		sendOrderedBroadcast(downIntent, null);
		sendOrderedBroadcast(upIntent, null);
	}

	public static void start(Context c) {
		Intent i = new Intent(c, DialogService.class);
		c.startService(i);
	}

	public static void stop(Context c) {
		Intent i = new Intent(c, DialogService.class);
		c.stopService(i);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		unregisterReceiver(mReceiver);
	}
}

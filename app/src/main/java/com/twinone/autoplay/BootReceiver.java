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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("BootReceiver", "Boot received, starting service");
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			if (isEnabled(context)) {
				DialogService.start(context);
			}
		}
	}

	private boolean isEnabled(Context c) {
		return c.getSharedPreferences(SettingsFragment.FILENAME,
				Context.MODE_PRIVATE).getBoolean(
				c.getString(R.string.pref_key_start_boot),
				c.getResources().getBoolean(R.bool.pref_def_start_boot));
	}
}

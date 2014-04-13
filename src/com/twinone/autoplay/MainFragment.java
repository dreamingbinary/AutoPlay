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

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

	private Button bToggleService;

	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_main, container, false);
		bToggleService = (Button) root.findViewById(R.id.bToggleService);
		bToggleService.setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bToggleService) {
			toggleService();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void toggleService() {
		if (!isServiceRunning()) {
			DialogService.start(getActivity());
		} else {
			DialogService.stop(getActivity());
		}
		updateLayout();
	}

	private void updateLayout() {
		if (isServiceRunning()) {
			bToggleService.setText(R.string.stop_service);
		} else {
			bToggleService.setText(R.string.start_service);
		}
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getActivity()
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (DialogService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
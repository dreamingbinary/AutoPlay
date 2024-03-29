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
package org.twinone.autoplay;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements OnClickListener {

	private Button mToggle;

	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View root = inflater.inflate(R.layout.fragment_main, container, false);
		mToggle = (Button) root.findViewById(R.id.swToggleService);
		mToggle.setOnClickListener(this);

		if (!isServiceRunning()) {
			DialogService.start(getActivity());
		}
		return root;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public void onClick(View arg0) {
		toggleService();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateLayout();

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
		boolean running = isServiceRunning();
		if (running) {
			mToggle.setText(R.string.stop_service);
		} else {
			mToggle.setText(R.string.start_service);
		}
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	private boolean isServiceRunning() {
		return DialogService.isRunning(getActivity());
	}
}
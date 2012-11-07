/*
 *  This file is part of Sensorium.
 *
 *   Sensorium is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Sensorium is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Sensorium. If not, see
 *   <http://www.gnu.org/licenses/>.
 * 
 * 
 */

package at.univie.sensorium;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import at.univie.sensorium.sensors.AbstractSensor;

public class SensorPreferenceActivity extends PreferenceActivity {

	SharedPreferences.OnSharedPreferenceChangeListener listener;

	CheckBoxPreference autostartPref;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
		SensorServiceSingleton.getInstance().bindService(this);
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

		PreferenceCategory generalCat = new PreferenceCategory(this);
		generalCat.setTitle("General Preferences");
		root.addPreference(generalCat);

		autostartPref = new CheckBoxPreference(this);
		autostartPref.setKey("sensor_autostart");
		autostartPref.setTitle("Sensor starts on boot");
		autostartPref.setSummary("Keeps the sensor service running at all times.");
		generalCat.addPreference(autostartPref);

		PreferenceCategory sensorsCat = new PreferenceCategory(this);
		sensorsCat.setTitle("Individual Sensors");
		root.addPreference(sensorsCat);

		List<AbstractSensor> sensors = SensorRegistry.getInstance().getSensors();

		for (AbstractSensor sensor : sensors) {
			SensorPreference sPref = new SensorPreference(this, sensor);
			sPref.setKey(sensor.getClass().getName() + "-level");
			sensorsCat.addPreference(sPref);
		}
		return root;
	}
}
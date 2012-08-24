/*
 *  * Copyright (C) 2012 Florian Metzger
 * 
 *  This file is part of android-seattle-sensors.
 *
 *   android-seattle-sensors is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   android-seattle-sensors is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with android-seattle-sensors. If not, see
 *   <http://www.gnu.org/licenses/>.
 * 
 * 
 */

package at.univie.seattlesensors.sensors;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import at.univie.seattlesensors.SensorRegistry;

public class GPSLocationSensor extends AbstractSensor {

	private LocationManager locationManager;
	private LocationListener locationListener;

	private Location location;
	private long timestamp;

	public GPSLocationSensor(Context context) {
		super(context);

		name = "GPS Loc Sensor";
		enable();
	}
	

	@Override
	public void enable() {
		
		locationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				location = loc;
				timestamp = System.currentTimeMillis();

				SensorRegistry.getInstance().debugOut(
						"t_fix: " + location.getTime() + "long: "
								+ location.getLongitude() + " lat: "
								+ location.getLatitude() + " alt: " + location.getAltitude()
								+ "spd: " + location.getSpeed() + "bearing: " + location.getBearing()
								+ "accuracy: " + location.getAccuracy() + "provider: "
								+ location.getProvider());
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
				Log.d("LocationSensor", provider
						+ " enabled, listening for updates.");
			}

			public void onProviderDisabled(String provider) {
				Log.d("LocationSensor", provider
						+ " disabled, no more updates.");
			}
		};

		locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		super.enable();
	}

	@Override
	public void disable() {
		super.disable();
		if (locationManager != null)
			locationManager.removeUpdates(locationListener);
		super.disable();
	}

	@XMLRPCMethod
	public Object[] gpslocationInformation(){
		if(location != null){
			return new Object[] {"timestamp", timestamp, "timestamp_fix", location.getTime(), "long", location.getLongitude(), "lat", location.getLatitude(), "alt", location.getAltitude(), "bearing", location.getBearing(), "speed", location.getSpeed(), "accuracy", location.getAccuracy()};
		}
		return null;
	}
}

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package firstmob.firstbank.com.firstagent.Activity.backgroundlocation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.Toast;


import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;


import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_LATITUDE;
import static firstmob.firstbank.com.firstagent.constants.SharedPrefConstants.KEY_LONGIT;

/**
 * Class to process location results.
 */
public class LocationResultHelper {

    public final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";

    final private static String PRIMARY_CHANNEL = "default";


    private Context mContext;
    private List<Location> mLocations;
    private NotificationManager mNotificationManager;

    LocationResultHelper(Context context, List<Location> locations) {
        mContext = context;
        mLocations = locations;

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PRIMARY_CHANNEL,"default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(channel);
        }

    }

    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */


    private String getLatitude(){
        String lat = "NA";
        if (mLocations.isEmpty()) {
            lat =  "NA";
        }else {
            for (Location location : mLocations) {
                lat = Double.toString(location.getLatitude());
            }
            }
            return lat;
        }

    private String getLongt(){
        String longt = "NA";
        if (mLocations.isEmpty()) {
            longt =  "NA";
        }else {
            for (Location location : mLocations) {
                longt = Double.toString(location.getLongitude());
            }
        }
        return longt;
    }
    private String getLocationResultText() {
        if (mLocations.isEmpty()) {
            return "Unknown location";
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : mLocations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults() {
       /* PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, "Latitude is "+getLatitude()+ "\n Longitude is" +
                        getLongt())
                .apply();*/

        Prefs.putString(KEY_LATITUDE,getLatitude());
        Prefs.putString(KEY_LONGIT,getLongt());

        Log.i("My Location is", "Latitude is "+getLatitude()+ "\n Longitude is" +
                getLongt());

        Toast.makeText(mContext,"Latitude is "+getLatitude()+ "\n Longitude is" +
                getLongt(),Toast.LENGTH_LONG).show();


    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
   public  static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }


}
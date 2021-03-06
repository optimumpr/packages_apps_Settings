/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.settings.du;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Gravity;

import java.util.ArrayList;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.util.Helpers;

public class Weather extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, OnPreferenceClickListener {
    private static final String TAG = "WeatherSettings";

    private static final String WEATHER_START = "weather_start";
    // Package name of the weather app
    public static final String WEATHER_PACKAGE_NAME = "com.android.settings";
    public static final String WEATHER_PACKAGE_NAME2 = "com.android.settings.du.weather";
    // Intent for launching the weather main actvity
    public static Intent INTENT_WEATHER = new Intent(Intent.ACTION_MAIN)
            .setClassName(WEATHER_PACKAGE_NAME, WEATHER_PACKAGE_NAME2 + ".WeatherActivity");

    private static final String PREF_SYSTEMUI_WEATHER_HEADER_VIEW = "cfx_systemui_header_weather_view";
    private static final String PREF_SYSTEMUI_WEATHER_NOTIFICATION = "cfx_weather_notification";

    private CheckBoxPreference mWeatherHeader;
    private CheckBoxPreference mWeatherNotification;

    private Preference mWeather;

    private Context Context;
    private PowerManager pm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.du_weather);

        PreferenceScreen prefSet = getPreferenceScreen();

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setIcon(R.drawable.ic_settings_dirt);

        mWeather = (Preference)
                prefSet.findPreference(WEATHER_START);

        boolean enableWeatherHeader = Settings.System.getBoolean(getContentResolver(),
                Settings.System.SYSTEMUI_WEATHER_HEADER_VIEW, false);
        mWeatherHeader = (CheckBoxPreference) prefSet.findPreference(PREF_SYSTEMUI_WEATHER_HEADER_VIEW);
        mWeatherHeader.setChecked(enableWeatherHeader);
        mWeatherHeader.setOnPreferenceChangeListener(this);

        boolean enableWeatherNotification = Settings.System.getBoolean(getContentResolver(),
                Settings.System.SYSTEMUI_WEATHER_NOTIFICATION, false);
        mWeatherNotification = (CheckBoxPreference) prefSet.findPreference(PREF_SYSTEMUI_WEATHER_NOTIFICATION);
        mWeatherNotification.setChecked(enableWeatherNotification);
        mWeatherNotification.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        ContentResolver resolver = getActivity().getContentResolver();
        boolean value;
        if (preference == mWeather) {
            startActivity(INTENT_WEATHER);
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        final String key = preference.getKey();
        if (preference == mWeatherHeader) {
            Settings.System.putBoolean(resolver,
                    Settings.System.SYSTEMUI_WEATHER_HEADER_VIEW,
                    ((Boolean) objValue) ? true : false);
            Helpers.restartSystemUI();
        } else if (preference == mWeatherNotification) {
            Settings.System.putBoolean(resolver,
                    Settings.System.SYSTEMUI_WEATHER_NOTIFICATION,
                    ((Boolean) objValue) ? true : false);
            Helpers.restartSystemUI();
        }

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}

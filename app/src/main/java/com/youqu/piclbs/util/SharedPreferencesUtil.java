/*
 *
 *  * Copyright 2015 Atanas Dimitrov <atanas@naughtyspirit.co>
 *  *                 NaughtySpirit 2014
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.youqu.piclbs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {

    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    private SharedPreferencesUtil() {
        throw new UnsupportedOperationException();
    }

    public static void putString(Context ctx, String key, String value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putInt(Context ctx, String key, int value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putLong(Context ctx, String key, long value) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(Context ctx, String key, String defaultVaule) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getString(key, defaultVaule);
    }

    public static int getInt(Context ctx, String key, int defaultValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static Boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static long getLong(Context ctx, String key, long defaultValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void remove(Context ctx, String key) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
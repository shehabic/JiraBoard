package com.fullmob.jiraboard.managers.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fullmob.jiraboard.managers.serializers.SerializerInterface;
import com.google.gson.reflect.TypeToken;


public class LocalStorageManager implements LocalStorageInterface {
    private SerializerInterface serializer;
    private SharedPreferences preferences;

    public LocalStorageManager(SerializerInterface serializer, Context context) {
        this.serializer = serializer;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public <T> T get(String key, Class<T> classOfT, T t) {
        String value = getString(key);

        return null != value ? serializer.deSerialize(value, classOfT) : t;
    }

    @Override
    public <T> T get(String key, TypeToken<T> typeToken, T defaultValue) {
        String value = getString(key);
        return value != null ? serializer.deSerialize(value, typeToken) : defaultValue;
    }

    @Override
    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    @Override
    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    @Override
    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    @Override
    public void delete(String key) {
        preferences.edit().remove(key).apply();
    }

    @Override
    public String getString(String key) {
        return getString(key, null);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
    @Override
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    @Override
    public long getFloat(String key, float defaultValue) {
        return 0;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

}

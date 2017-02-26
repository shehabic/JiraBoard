package com.fullmob.jiraboard.managers.storage;

import com.google.gson.reflect.TypeToken;

public interface LocalStorageInterface {
    <T> T get(String key, Class<T> classOfT, T t);

    <T> T get(String key, TypeToken<T> typeToken, T defaultValue);

    String getString(String key);

    String getString(String key, String defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    int getInt(String key, int defaultValue);

    long getLong(String key, long defaultValue);

    long getFloat(String key, float defaultValue);

    void putInt(String key, int value);

    void putFloat(String key, float value);

    void putLong(String key, long value);

    void delete(String key);

    void putBoolean(String key, boolean value);

    void putString(String key, String value);
}

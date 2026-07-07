package com.gersseba.garden.i18n;

import org.junit.Before;
import org.junit.Test;

import android.content.SharedPreferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SettingsDataStoreImplTest {

    private SettingsDataStoreImpl dataStore;
    private FakeSharedPreferences fakePrefs;

    @Before
    public void setUp() {
        fakePrefs = new FakeSharedPreferences();
        dataStore = new SettingsDataStoreImpl(fakePrefs);
    }

    @Test
    public void returnsNullInitially() {
        assertNull(dataStore.getSavedLocale());
    }

    @Test
    public void savesAndRetrievesLocale() {
        dataStore.saveLocale("en-US");
        assertEquals("en-US", fakePrefs.getString("app.language", null));
        assertEquals("en-US", dataStore.getSavedLocale());

        dataStore.saveLocale("de-DE");
        assertEquals("de-DE", dataStore.getSavedLocale());
    }

    private static class FakeSharedPreferences implements SharedPreferences {
        private final Map<String, Object> data = new HashMap<>();
        @Override public String getString(String key, String def) { return (String) data.getOrDefault(key, def); }
        @Override public Editor edit() { return new FakeEditor(this); }
        @Override public Map<String, ?> getAll() { return data; }
        @Override public boolean getBoolean(String key, boolean def) { return (Boolean) data.getOrDefault(key, def); }
        @Override public float getFloat(String key, float def) { return (Float) data.getOrDefault(key, def); }
        @Override public int getInt(String key, int def) { return (Integer) data.getOrDefault(key, def); }
        @Override public long getLong(String key, long def) { return (Long) data.getOrDefault(key, def); }
        @Override public boolean contains(String key) { return data.containsKey(key); }
        @Override public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
        @Override public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {}
        @Override public Set<String> getStringSet(String key, Set<String> def) { return (Set<String>) data.getOrDefault(key, def); }

        private static class FakeEditor implements Editor {
            private final FakeSharedPreferences parent;
            private final Map<String, Object> temp = new HashMap<>();
            FakeEditor(FakeSharedPreferences parent) { this.parent = parent; }
            @Override public Editor putString(String key, String value) { temp.put(key, value); return this; }
            @Override public boolean commit() { parent.data.putAll(temp); return true; }
            @Override public void apply() { commit(); }
            @Override public Editor clear() { temp.clear(); parent.data.clear(); return this; }
            @Override public Editor remove(String key) { temp.remove(key); parent.data.remove(key); return this; }
            @Override public Editor putBoolean(String key, boolean value) { temp.put(key, value); return this; }
            @Override public Editor putFloat(String key, float value) { temp.put(key, value); return this; }
            @Override public Editor putInt(String key, int value) { temp.put(key, value); return this; }
            @Override public Editor putLong(String key, long value) { temp.put(key, value); return this; }
            @Override public Editor putStringSet(String key, Set<String> values) { temp.put(key, values); return this; }
        }
    }
}




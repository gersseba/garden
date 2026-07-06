package com.gersseba.garden.i18n;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsSharedPreferencesDataStoreTest {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SettingsSharedPreferencesDataStore dataStore;

    @Before
    public void setUp() {
        sharedPreferences = org.mockito.Mockito.mock(SharedPreferences.class);
        editor = org.mockito.Mockito.mock(SharedPreferences.Editor.class);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);

        dataStore = new SettingsSharedPreferencesDataStore(sharedPreferences);
    }

    @Test
    public void getSavedLocaleReturnsValueFromPrefs() {
        when(sharedPreferences.getString(eq("app.language"), (String) org.mockito.ArgumentMatchers.isNull())).thenReturn("en");
        assertEquals("en", dataStore.getSavedLocale());
    }

    @Test
    public void saveLocaleUpdatesPrefs() {
        dataStore.saveLocale("de");
        verify(editor).putString("app.language", "de");
        verify(editor).apply();
    }
}





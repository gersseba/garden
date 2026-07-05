package com.gersseba.garden.i18n

import android.content.SharedPreferences
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class SettingsSharedPreferencesDataStoreTest {

    class SimpleSharedPreferences : SharedPreferences {
        var saved: String? = null

        override fun getString(key: String?, defValue: String?): String? = saved ?: defValue

        override fun edit(): SharedPreferences.Editor = object : SharedPreferences.Editor {
            override fun putString(key: String?, value: String?): SharedPreferences.Editor {
                saved = value
                return this
            }

            override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor = this
            override fun putInt(key: String?, value: Int): SharedPreferences.Editor = this
            override fun putLong(key: String?, value: Long): SharedPreferences.Editor = this
            override fun putFloat(key: String?, value: Float): SharedPreferences.Editor = this
            override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor = this
            override fun remove(key: String?): SharedPreferences.Editor { saved = null; return this }
            override fun clear(): SharedPreferences.Editor { saved = null; return this }
            override fun commit(): Boolean = true
            override fun apply() {}
        }

        // Unused methods for this simple mock
        override fun getAll(): MutableMap<String, *>? = null
        override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? = defValues
        override fun getInt(key: String?, defValue: Int): Int = defValue
        override fun getLong(key: String?, defValue: Long): Long = defValue
        override fun getFloat(key: String?, defValue: Float): Float = defValue
        override fun getBoolean(key: String?, defValue: Boolean): Boolean = defValue
        override fun contains(key: String?): Boolean = saved != null
        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
    }

    @Test
    fun saveAndReadLocale() {
        val prefs = SimpleSharedPreferences()
        val store = SettingsSharedPreferencesDataStore(prefs)

        runBlocking {
            assertNull(store.getSavedLocale())
            store.saveLocale("de")
            assertEquals("de", store.getSavedLocale())
        }
    }
}








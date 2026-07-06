package com.gersseba.garden.i18n;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SettingsDataStoreImplTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private SettingsDataStoreImpl dataStore;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        testFile = temporaryFolder.newFile("test_settings.preferences_pb");
        dataStore = SettingsDataStoreImpl.createForTest(testFile);
    }

    @Test
    public void returnsNullInitially() {
        assertNull(dataStore.getSavedLocale());
    }

    @Test
    public void savesAndRetrievesLocale() {
        dataStore.saveLocale("en-US");
        assertEquals("en-US", dataStore.getSavedLocale());

        dataStore.saveLocale("de-DE");
        assertEquals("de-DE", dataStore.getSavedLocale());
    }
}


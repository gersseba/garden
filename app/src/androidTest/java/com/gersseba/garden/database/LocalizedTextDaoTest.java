package com.gersseba.garden.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gersseba.garden.database.entity.LocalizedTextEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class LocalizedTextDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void insertAndQuery_byEntityAndKey() throws Exception {
        LocalizedTextEntity e = new LocalizedTextEntity("plant", 1L, "general_info", "english text", "deutsch text", System.currentTimeMillis());
        long id = database.localizedTextDao().insertOrUpdate(e);
        assertNotNull(id);

        LocalizedTextEntity found = database.localizedTextDao().getByEntityAndKeySync("plant", 1L, "general_info");
        assertNotNull(found);
        assertEquals("english text", found.textEn);
        assertEquals("deutsch text", found.textDe);
    }
}


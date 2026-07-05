package com.gersseba.garden.repository;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gersseba.garden.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class LocalizedTextRepositoryRoomTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private LocalizedTextRepository repository;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        repository = new LocalizedTextRepository(database.localizedTextDao(), Runnable::run);
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void upsertAndRetrieve_withLocaleFallback() throws InterruptedException {
        repository.upsertLocalizedText("plant", 5L, "general_info", "english text", "deutsch text");

        LiveData<String> live = repository.getLocalizedTextLive("plant", 5L, "general_info", Locale.GERMAN);
        String out = awaitValue(live);
        assertEquals("deutsch text", out);

        LiveData<String> liveEn = repository.getLocalizedTextLive("plant", 5L, "general_info", Locale.FRENCH);
        String outEn = awaitValue(liveEn);
        assertEquals("english text", outEn);
    }

    private static <T> T awaitValue(LiveData<T> liveData) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final Object[] holder = new Object[1];
        liveData.observeForever(value -> {
            holder[0] = value;
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
        return (T) holder[0];
    }
}

